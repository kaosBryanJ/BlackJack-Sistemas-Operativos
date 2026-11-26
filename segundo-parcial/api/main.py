import os

import httpx
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field


app = FastAPI(
    title="BlackJack Distributed API",
    version="1.0.0"
)

PROCESSOR_URL = os.getenv(
    "PROCESSOR_URL",
    "http://processor:8001"
)


class Operation(BaseModel):
    partida_id: str = Field(min_length=1, max_length=50)
    jugador: str = Field(min_length=1, max_length=50)
    accion: str = Field(min_length=1, max_length=20)


@app.get("/")
def root():
    return {
        "servicio": "blackjack-api",
        "estado": "activo"
    }


@app.get("/health")
def health():
    return {
        "status": "ok",
        "service": "api"
    }


@app.post("/operaciones")
async def create_operation(operation: Operation):

    accion = operation.accion.upper()

    if accion not in {"HIT", "STAND"}:
        raise HTTPException(
            status_code=400,
            detail="La accion debe ser HIT o STAND"
        )

    payload = {
        "partida_id": operation.partida_id,
        "jugador": operation.jugador,
        "accion": accion
    }

    try:
        async with httpx.AsyncClient(timeout=5.0) as client:

            response = await client.post(
                f"{PROCESSOR_URL}/procesar",
                json=payload
            )

            response.raise_for_status()

            return response.json()

    except httpx.HTTPError as error:

        raise HTTPException(
            status_code=503,
            detail=f"Procesador no disponible: {error}"
        )
