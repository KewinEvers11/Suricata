package org.oyabun.suricata.mappers;

import java.util.Objects;
import java.util.UUID;

public class MapperUtils {

    public static UUID stringToUuid(String uuidStr) {
        return Objects.nonNull(uuidStr) ? UUID.fromString(uuidStr) : null;
    }

    public static String uuidToString(UUID uuid) {
        return Objects.nonNull(uuid) ? uuid.toString():null;
    }
}
