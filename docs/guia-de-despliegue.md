# Banco Internacional - Deploy Guide

Este documento es una guía de despliegue para la aplicación del desafio técnico propuesto
por produbanco, se realizó con el framewrok Spring Boot y fue empaquetado como WAR, para la
persisrencia de datos usa PostgreSQL, el despliegue se lo hace usando Docker Compose.

---

## Requerimientos Previos

1. Docker (>= 24.x)
2. Docker Compose (v2)
3. Java 21 y Maven para generar WAR (si no está precompilado)

---

## Estructura del Proyecto

```
.
├── docker-compose.yml
├── .env
├── target/
│   └── banco-internacional-prueba-tecnica-1.0.0.war
└── README.md
```

---

## Generación del WAR

Para generar el archivo WAR, ejecutar el siguiente comando en la raíz del proyecto:

```bash
mvn clean package
```

En caso muy excepcional de que falle alguno de los tests, o si quiere omitirlos se puede usar:

```bash
mvn clean package -DskipTests
```

El WAR se ubicará en `./target/banco-internacional-prueba-tecnica-0.0.1.war`

---

## Configuración de Variables (.env)

```dotenv
# Database
POSTGRES_DB=banco_internacional
POSTGRES_USER=postgres
POSTGRES_PASSWORD=123456

# App
SPRING_APPLICATION_NAME=banco-internacional-prueba-tecnica
CUSTOMER_NUMBER_CIPHER_KEY=12345678901234567890123456789012
CUSTOMER_NUMBER_CIPHER_ALGORITHM=AES
SPRING_DATASOURCE_URL="jdbc:postgresql://db:5432/${POSTGRES_DB}"
SPRING_DATASOURCE_USERNAME="${POSTGRES_USER}"
SPRING_DATASOURCE_PASSWORD="${POSTGRES_PASSWORD}"
SPRINGDOC_API_DOCS_ENABLED=false
```

Se recomienda modificar las variables según el entorno de despliegue, especialmente
las credenciales de la base de datos y las variables de cifrado, tenga en cuenta que el tamaño
de la llave debe ser compatible con el algoritmo de cifrado.

Para entornos de producción, se recomienda desactivar el acceso a la documentación de la API
para no exponer información de la aplicación.

---

## Levantar la Aplicación

```bash
docker compose up -d
```

* La app estará disponible por defecto en [http://localhost:8080](http://localhost:8080)
* Ver logs:

```bash
docker compose logs -f spring-war-app
```

Debe visualizarse un mensaje similar a este en los logs:

![logs.png](assets/logs.png)

* Detener contenedores:

```bash
docker compose down
```

---

## Notas

* El puerto 8080 debe estar libre en el host, o caso contrario modificar el puerto en el archivo
  `docker-compose.yml`.

## OpenAPI

Si se desea habilitar la documentación de la API REST, modificar la variable
`SPRINGDOC_API_DOCS_ENABLED` a `true` en el archivo `.env`.
Luego acceder al siguiente enlace para ver la documentación generada automáticamente:

[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Esta documentación puede ser importada en postman o en cualquier otro cliente que soporte
la especificación OpenApi.
