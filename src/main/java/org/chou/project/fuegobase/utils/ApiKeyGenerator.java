package org.chou.project.fuegobase.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ApiKeyGenerator {
    public String generateApiKey() {
        return UUID.randomUUID().toString();
    }
}
