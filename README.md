# Suricata
API REST para monitorear precios de productos.

# Technologies:
* **Api REST:** Spring Boot 4.1.1
* **Database:** Postgresql 18.6

# Configuración inicial base de datos

Descargar Postgresql 18.6. 

## Instalación usando docker.

Para ejecutar el proyecto de forma local, puedes usar Docker.
1. Debes instalar una herramienta para ejecutar contenedores de Docker, puede ser docker desktop, rancher, etc.
2. Instalar la Docker CLI
3. Ejecuta el siguiente comando para crear el contenedor con la base de datos configurada según `src/main/resources/application-develop.yaml`:
   ```shell
   docker run --name suricata-postgres \
     -e POSTGRES_DB=suricata_app \
     -e POSTGRES_USER=suricata_appuser \
     -e POSTGRES_PASSWORD=sur1c4t4 \
     -p 5432:5432 \
     -v suricata-pgdata:/var/lib/postgresql \
     -d postgres:18.6
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
