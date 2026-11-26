# Segundo Parcial - BlackJack

## Arquitectura

El sistema está compuesto por tres servicios principales:

- API: recibe las operaciones de Blackjack.
- Processor: procesa y persiste las operaciones.
- PostgreSQL: almacena las operaciones.

La comunicación principal es:

Cliente -> API -> Processor -> PostgreSQL

El despliegue se realizó mediante Docker y Kubernetes utilizando Minikube.

## Componentes

- `api/`: servicio REST desarrollado con FastAPI.
- `processor/`: procesamiento y persistencia de operaciones.
- `database/`: script de inicialización de PostgreSQL.
- `k8s/`: Deployments, Services, Secret, ConfigMap y PVC.
- `tests/`: pruebas de carga con Locust.
- `results/`: resultados locales generados por Locust.

## Prueba funcional

Se realizó una operación mediante:

`POST /operaciones`

Ejemplo utilizado:

- partida: K8S-004
- jugador: Jugador 4
- accion: HIT

Resultado:

- operación procesada correctamente.
- operation_id: 51ba62b5-ef17-4f01-b1f9-a8ac15fe1e2e
- processor_id: blackjack-processor-59d8845dd-c2tnp

La operación fue posteriormente verificada en PostgreSQL.

## Pruebas de carga

### E1-R1

- Usuarios: 5
- Tiempo: 30 segundos
- Peticiones: 336
- Errores: 0
- Tasa de error: 0.00%
- Promedio: 30 ms
- Máximo: 1250 ms
- Requests/s: 12.14

### E1-R2

- Usuarios: 5
- Tiempo: 30 segundos
- Peticiones: 136
- Errores: 3
- Tasa de error: 2.21%
- Promedio: 735 ms
- Máximo: 22062 ms
- Requests/s: 3.69

Los errores fueron HTTP 503 debido a que el Processor no estaba disponible durante parte de la prueba.

### E1-R2-FIX

Después de ajustar los probes de Kubernetes y volver a desplegar API y Processor:

- Usuarios: 5
- Tiempo: 30 segundos
- Peticiones: 154
- Errores: 0
- Tasa de error: 0.00%
- Promedio: 467 ms
- Mediana: 34 ms
- Máximo: 9781 ms
- Requests/s: 5.65

Durante la prueba corregida:

- API: Running, 0 reinicios
- Processor: Running, 0 reinicios
- PostgreSQL: Running, sin nuevos reinicios durante la prueba

## Conclusión

La prueba E1-R2 permitió detectar un problema de disponibilidad del Processor bajo carga. Después de modificar la configuración de los probes de Kubernetes y realizar un nuevo despliegue, la prueba E1-R2-FIX terminó con 0 errores HTTP 503 y los servicios API y Processor permanecieron estables.

Los resultados de Locust se conservan localmente en `results/`.
