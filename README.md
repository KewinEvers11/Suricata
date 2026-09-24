# Suricata
API REST para monitorear precios de productos.

# Technologies:
* **Api REST:** Spring Boot 4.1.1
* **Database:** Postgresql 18.6
* **Java:** JDK 21

# Requisitos previos

* Tener instalado **JDK 21**. Puedes descargarlo desde [Adoptium Temurin](https://adoptium.net/es/temurin/releases/?version=21).

# Configuración inicial base de datos

Descargar Postgresql 18.6. 

## Instalación local para usar docker

Para ejecutar el proyecto de forma local, puedes usar Docker.
1. Debes instalar una herramienta para ejecutar contenedores de Docker, puede ser docker desktop, rancher, etc.
2. Instalar la Docker CLI
3. Ejecuta el siguiente comando para crear el contenedor con la base de datos configurada según `src/main/resources/application-develop.yaml`:
   ```shell
   docker run --name suricata-postgres -e POSTGRES_DB=suricata_app -e POSTGRES_USER=suricata_appuser -e POSTGRES_PASSWORD=sur1c4t4 -p 5432:5432 -v suricata-pgdata:/var/lib/postgresql -d postgres:18.6
   ```

   > **Nota:** desde PostgreSQL 18, la imagen oficial monta el volumen en `/var/lib/postgresql` (antes era `/var/lib/postgresql/data`). Montar en la ruta antigua provoca errores al iniciar el contenedor.
4. Para verificar que la base de datos responde:
   ```shell
   docker exec -it suricata-postgres psql -U suricata_appuser -d suricata_app
   ```
5. Para ejecutar el proyecto con el perfil `develop`:
   ```shell
   ./gradlew bootRun --args='--spring.profiles.active=develop'
   ```

# Ejecutar usando docker compose LOCAL

Con Docker Compose se puede levantar la aplicación junto con su base de datos PostgreSQL
en una red interna, sin necesidad de tener PostgreSQL instalado localmente. La
configuración de ambos contenedores (credenciales, puertos y nombres) vive
directamente en `docker-compose.yml`.

## Requisitos previos

Para **ejecutar** los servicios necesitas:

* Una herramienta para ejecutar contenedores de Docker (Docker Desktop, Rancher,
  etc.).
* La Docker CLI disponible.

Para **generar** la imagen de la aplicación (paso 1) además necesitas:

* **JDK 21**: `bootBuildImage` primero compila el proyecto (`compileJava` →
  `bootJar`) usando el toolchain de Java 21 configurado en `build.gradle`. El
  Java que se ejecuta *dentro* de la imagen lo provee el buildpack
  (BellSoft Liberica JRE 21) y es independiente del JDK local.

## Pasos

1. **Genera la imagen de la aplicación.** ejecutando este comando en el proyecto
   ```shell
   ./gradlew bootBuildImage
   ```
   Esto produce la imagen `suricata-app:latest`.

2. **Levanta los servicios.** Desde la raíz del proyecto:
   ```shell
   docker compose up -d
   ```
   Se inician dos contenedores sobre la red interna `suricata-network`:
   * `suricata-postgres`: la base de datos `postgres:18.6`.
   * `suricata-app`: la aplicación Spring Boot.

   La aplicación arranca solo cuando PostgreSQL está saludable (healthcheck con
   `pg_isready`), y Flyway aplica las migraciones automáticamente al iniciar.

3. **Accede a la aplicación.** Una vez levantados los servicios, la API queda
   disponible en:
   ```
   http://localhost:8080/suricata-app
   ```
   La documentación Swagger UI en:
   ```
   http://localhost:8080/suricata-app/swagger-ui/index.html
   ```

## Comandos útiles

| Acción | Comando |
|---|---|
| Levantar en segundo plano | `docker compose up -d` |
| Ver logs | `docker compose logs -f` |
| Detener los contenedores | `docker compose down` |
| Detener y borrar los datos | `docker compose down -v` |
| Reconstruir la imagen tras cambios | `./gradlew bootBuildImage && docker compose up -d` |
