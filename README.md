# aemet-simple-viewer

## Tecnologías

- *Backend*: Java 25 + Spring Boot 4.1
- *Frontend*: Angular 22 + SASS

## Ejecución

Las imagenes se publican en GHCR cada vez que se hace push a `main`, asi que no hace falta clonar ni compilar nada para probarlo, solo tener Docker.

Creamos una carpeta 'aemet' y metemos nuestra API KEY en el archivo `.env`:

```bash
echo "AEMET_API_KEY=tu_api_key" > .env
```

Despues descargamos las imagenes y levantamos los dos contenedores en la misma red. El backend tiene que llamarse `backend` porque el nginx del frontend redirige `/api` a `http://backend:8080`.

```bash
docker pull ghcr.io/bleussa/aemet-backend:latest
docker pull ghcr.io/bleussa/aemet-frontend:latest

docker network create aemet

docker run -d --name backend --network aemet --env-file .env -p 8080:8080 ghcr.io/bleussa/aemet-backend:latest
docker run -d --name frontend --network aemet -p 4200:80 ghcr.io/bleussa/aemet-frontend:latest
```

Con eso la aplicación queda en el localhost:4200. Tambien se puede probar la API directamente:

```bash
curl "http://localhost:8080/api/municipios?nombre=madrid"
curl "http://localhost:8080/api/prediccion/28079"
curl "http://localhost:8080/api/prediccion/28079?unidad=G_FAH"
```

Para pararlo y limpiar todo ejecutamos:

```bash
docker rm -f frontend backend
docker network rm aemet
```

## Preguntas

### 1. ¿Cómo haríamos para mejorar el tiempo de respuesta de este servicio?

Lo primero que note es que la lentitud viene de dos cosas: por un lado, AEMET obliga a hacer dos llamadas (la primera te devuelve una URL y la segunda los datos reales) y, por otro, el listado tiene mas de 8000 municipios. Si cada vez que el usuario escribe en el autocompletado vamos a AEMET a pedir todo eso, la respuesta va a ser lenta siempre. Ademas el listado de municipios practicamente no cambia asi que no tiene sentido pedirlo una y otra vez.

Las dos opciones que veo son:

- *Cache en memoria*: la primera vez que se piden los municipios se guardan en memoria en el backend y, a partir de ahi, las busquedas se hacen sobre esa lista sin volver a llamar a AEMET. Como la lista casi no cambia, se le puede poner una caducidad larga (por ejemplo 24 horas) para refrescarla de vez en cuando. Filtrar 8000 municipios en memoria es rapido, asi que la busqueda pasa a ser practicamente instantanea. Lo implemente con Caffeine en el adaptador de AEMET.

- *Persistencia local:* ir un paso mas alla y guardar el listado en una base de datos y sincronizarlo con AEMET cada cierto tiempo (por ejemplo una vez al dia). La ventaja frente a la cache en memoria es que los datos no se pierden si se reinicia el backend. Con la arquitectura hexagonal seria añadir otro adaptador que implemente el mismo puerto `MunicipioProvider`, sin tocar el resto.

### 2. ¿Por qué motivo piensas que AEMET ha implementado este servicio así?

La verdad no conocia ni habia visto esta forma de responder de una API, asi que tuve que buscar e investigar un poco para entender por que AEMET lo hace asi.

Por lo que entendi, la idea es separar dos cosas: por un lado, la API, que se encarga de validar la API key, controlar las cuotas y la petición y por otro, la entrega de los datos, que es simplemente servir un archivo que ya esta generado.

Las predicciones no se calculan cada vez que alguien las pide, sino que se generan cada cierto tiempo y se dejan publicadas como archivos. Si muchos usuarios piden la predicción del mismo municipio, todos reciben el mismo archivo. Servir archivos ya generados es mucho mas barato y escalable, y la parte "costosa" a nivel de computo (autenticar y controlar el uso) queda separada.

### 3. ¿Como recordar el ultimo municipio y cargar automaticamente su prediccion?

La forma mas simple es hacerlo en el frontend: guardar en `localStorage` el ultimo municipio seleccionado (código y nombre) y la unidad de temperatura. Al abrir la aplicación se leen esos datos, se rellenan los campos y se carga la prediccion automaticamente.

No hace falta tocar el backend. El unico limite es que queda guardado solo en ese navegador; si en el futuro hubiera usuarios registrados, se podria guardar como preferencia del usuario en el backend.

### 4. ¿Cómo reducir la carga sobre AEMET afectando lo minimo a los usuarios?

La mayoria de las llamadas que hacemos a AEMET son repetidas: los mismos municipios y las mismas predicciones, que solo cambian unas pocas veces al dia. Lo mas rapido y efectivo es cachear tambien las predicciones por municipio con una caducidad corta (por ejemplo, 1 hora). Asi, el numero de llamadas a AEMET deja de depender de cuantos usuarios tenemos y pasa a depender de cuantos municipios distintos se consultan.

Si la aplicación sigue creciendo y hay varias instancias del backend, pasaria esa cache a Redis para que sea compartida. Tambien añadiria un limite de llamadas por segundo hacia AEMET y, si AEMET falla, devolveria la ultima predicción que tengamos guardada en lugar de un error pero avisando el inconveniente.

Todo esto se resuelve en la capa de infraestructura, sobre los adaptadores de AEMET, sin tocar el dominio ni el frontend, asi que para el usuario la aplicación sigue funcionando igual o incluso mas rapido.
