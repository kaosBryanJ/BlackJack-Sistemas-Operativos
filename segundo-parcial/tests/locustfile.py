import os
import uuid

from locust import HttpUser, task, between


RUN_ID = os.getenv("RUN_ID", "manual")


class BlackjackUser(HttpUser):

    wait_time = between(0.2, 0.5)

    @task
    def registrar_operacion(self):

        accion = "HIT" if uuid.uuid4().int % 2 == 0 else "STAND"

        payload = {
            "partida_id": f"{RUN_ID}-{uuid.uuid4().hex[:8]}",
            "jugador": f"Jugador-{uuid.uuid4().hex[:6]}",
            "accion": accion
        }

        with self.client.post(
            "/operaciones",
            json=payload,
            name="POST /operaciones",
            catch_response=True
        ) as response:

            if response.status_code != 200:
                response.failure(
                    f"HTTP {response.status_code}: {response.text[:200]}"
                )
