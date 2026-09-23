ALTER TABLE solicitud_monitoreo
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

-- REVERT SCRIPT
-- ALTER TABLE solicitud_monitoreo DROP COLUMN version;
-- DELETE FROM flyway_schema_history WHERE "version" = '1.02';
