package com.pedrcpereira.nkbeauty.common.exception;

import java.util.Map;

public class ConflictException extends RuntimeException {
    private final Map<String, Object> properties;

    public ConflictException(String message) {
        super(message);
        this.properties = Map.of();
    }

    public ConflictException(String message, Map<String, Object> properties) {
        super(message);
        this.properties = Map.copyOf(properties);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
