import os
import uuid
from contextlib import asynccontextmanager
from datetime import datetime, timezone

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from psycopg_pool import AsyncConnectionPool


DB_HOST = os.getenv("DB_HOST", "blackjack-postgres")
DB_PORT = os.getenv("DB_PORT", "5432")
DB_NAME = os.getenv("DB_NAME", "blackjack")
DB_USER = os.getenv("DB_USER", "blackjack")
DB_PASSWORD = os.getenv("DB_PASSWORD", "blackjack123")

PROCESSOR_ID = os.getenv(
    "PROCESSOR_ID",
    "processor-local"
)

DATABASE_URL = (
    f"postgresql://{DB_USER}:{DB_PASSWORD}"
    f"@{DB_HOST}:{DB_PORT}/{DB_NAME}"
)


pool = AsyncConnectionPool(
    conninfo=DATABASE_URL,
    min_size=1,
    max_size=10,
    open=False
)


@asynccontextmanager
async def lifespan(app: FastAPI):

    await pool.open()
    await pool.wait(timeout=15)

    yield

    await pool.close()


app = FastAPI(
    title="BlackJack Processor",
    version="1.0.0",
    lifespan=lifespan
)


class Operation(BaseModel):

    partida_id: str = Field(
        min_length=1,
        max_length=50
    )

    jugador: str = Field(
        min_length=1,
        max_length=50
    )

    accion: str = Field(
        min_length=1,
        max_length=20
    )


@app.get("/")
async def root():

    return {
        "servicio": "blackjack-processor",
        "estado": "activo",
        "processor_id": PROCESSOR_ID
    }


@app.get("/health")
async def health():

    try:

        async with pool.connection() as conn:

            await conn.execute("SELECT 1")

        return {
            "status": "ok",
            "service": "processor",
            "database": "connected",
            "processor_id": PROCESSOR_ID
        }

    except Exception as error:

        raise HTTPException(
            status_code=503,
            detail=f"Base de datos no disponible: {error}"
        )


@app.post("/procesar")
async def process_operation(
    operation: Operation
):

    accion = operation.accion.upper()

    if accion not in {"HIT", "STAND"}:

        raise HTTPException(
            status_code=400,
            detail="La accion debe ser HIT o STAND"
        )

    operation_id = uuid.uuid4()

    created_at = datetime.now(timezone.utc)

    try:

        async with pool.connection() as conn:

            await conn.execute(
                """
                INSERT INTO operaciones (
                    operation_id,
                    partida_id,
                    jugador,
                    accion,
                    processor_id,
                    created_at
                )
                VALUES (
                    %s,
                    %s,
                    %s,
                    %s,
                    %s,
                    %s
                )
                """,
                (
                    operation_id,
                    operation.partida_id,
                    operation.jugador,
                    accion,
                    PROCESSOR_ID,
                    created_at
                )
            )

        return {
            "status": "procesada",
            "operation_id": str(operation_id),
            "partida_id": operation.partida_id,
            "jugador": operation.jugador,
            "accion": accion,
            "processor_id": PROCESSOR_ID,
            "timestamp": created_at.isoformat()
        }

    except Exception as error:

        raise HTTPException(
            status_code=500,
            detail=f"No fue posible guardar la operacion: {error}"
        )
