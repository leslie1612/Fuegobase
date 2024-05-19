package org.chou.project.fuegobase.controller.security;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.exception.APIKeyException;
import org.chou.project.fuegobase.service.APIKeyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/security/project")
public class APIKeyController {
    private final APIKeyService apiKeyService;

    public APIKeyController(APIKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/key/rotation/{id}")
    public ResponseEntity<?> getNewAPIKey(@PathVariable("id") String projectId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(apiKeyService.generateNewKey(projectId));
        } catch (APIKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Project Not Found."));
        }
    }

    @GetMapping("/key/{id}")
    public ResponseEntity<?> getAllAPIKey(@PathVariable("id") String projectId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(apiKeyService.getAllAPIKey(projectId)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Project Not Found."));
        }
    }

    @DeleteMapping("/key/{key}")
    public ResponseEntity<?> deleteAPIKey(@PathVariable("key") String oldKey) {
        apiKeyService.deleteKey(oldKey);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
