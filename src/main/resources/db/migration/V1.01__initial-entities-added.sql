CREATE TABLE solicitud_monitoreo (
    id UUID PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    url_producto VARCHAR(255),
    nombre_usuario VARCHAR(255),
    revisor VARCHAR(255),
    estado VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE producto_monitoreado (
    id UUID PRIMARY KEY DEFAULT UUIDV7(),
    nombre VARCHAR(255) NOT NULL,
    marca VARCHAR(127) NOT NULL,
    modelo VARCHAR(127) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE sitio (
    id UUID PRIMARY KEY DEFAULT UUIDV7(),
    nombre VARCHAR(255) NOT NULL,
    base_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE producto_sitio (
    id UUID PRIMARY KEY DEFAULT UUIDV7(),
    ultimo_precio DOUBLE PRECISION,
    producto_id UUID,
    sitio_id UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE registro_precio_producto (
    tiempo_registro TIMESTAMP WITH TIME ZONE NOT NULL,
    precio DOUBLE PRECISION,
    producto_sitio_id UUID
);

ALTER TABLE producto_sitio ADD CONSTRAINT fk_producto_monitoreado_id
    FOREIGN KEY (producto_id) REFERENCES producto_monitoreado(id);

ALTER TABLE producto_sitio ADD CONSTRAINT fk_sitio_id
    FOREIGN KEY (sitio_id) REFERENCES sitio(id);

ALTER TABLE registro_precio_producto ADD CONSTRAINT fk_producto_sitio
    FOREIGN KEY (producto_sitio_id) REFERENCES producto_sitio(id);

-- REVERT SCRIPT
-- DELETE FROM flyway_schema_history WHERE "version" = '1.01';
-- ALTER TABLE registro_precio_producto DROP CONSTRAINT fk_producto_sitio;
-- ALTER TABLE producto_sitio DROP CONSTRAINT fk_sitio_id;
-- ALTER TABLE producto_sitio DROP CONSTRAINT fk_producto_monitoreado_id;
-- DROP TABLE registro_precio_producto;
-- DROP TABLE producto_sitio;
-- DROP TABLE sitio;
-- DROP TABLE producto_monitoreado;
-- DROP TABLE solicitud_monitoreo;
