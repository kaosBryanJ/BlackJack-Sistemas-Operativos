CREATE TABLE IF NOT EXISTS operaciones (
    operation_id UUID PRIMARY KEY,
    partida_id VARCHAR(50) NOT NULL,
    jugador VARCHAR(50) NOT NULL,
    accion VARCHAR(20) NOT NULL
        CHECK (accion IN ('HIT', 'STAND')),
    processor_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_operaciones_partida_id
ON operaciones (partida_id);
