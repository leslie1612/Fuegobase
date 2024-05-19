package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/collections/{collectionId}/documents/{documentId}/fields")
public class FieldController {
    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping
    public ResponseEntity<?> createField(@PathVariable String projectId, @PathVariable String collectionId,
                                         @PathVariable String documentId, @RequestBody FieldData fieldData) {
        try {
            fieldService.createField(projectId, collectionId, documentId, fieldData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Field not found"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }

    @GetMapping
    public ResponseEntity<?> getFields(@PathVariable String projectId, @PathVariable String collectionId,
                                       @PathVariable String documentId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.getFields(projectId, collectionId, documentId)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Field not found"));
        }
    }

    @PatchMapping("/{fieldId}/value/{valueId}")
    public ResponseEntity<?> updateField(@PathVariable String projectId, @PathVariable String collectionId,
                                         @PathVariable String documentId, @PathVariable String fieldId,
                                         @PathVariable String valueId, @RequestBody ValueInfoData valueInfoData) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.updateField(
                            projectId, collectionId, documentId, fieldId, valueId, valueInfoData)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Field not found"));
        }
    }

    @PostMapping("/{fieldId}")
    public ResponseEntity<?> addFieldValue(@PathVariable String projectId, @PathVariable String collectionId,
                                           @PathVariable String documentId, @PathVariable String fieldId,
                                           @RequestBody ValueInfoData valueInfoData) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.addFieldValue(
                            projectId, collectionId, documentId, fieldId, valueInfoData)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Field not found"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }


    @DeleteMapping("/{fieldId}")
    public ResponseEntity<?> deleteField(@PathVariable String projectId, @PathVariable String collectionId,
                                         @PathVariable String documentId, @PathVariable String fieldId,
                                         @RequestParam(required = false) String valueId) {
        try {
            fieldService.deleteField(projectId, collectionId, documentId, fieldId, valueId);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Field not found"));
        }

    }
}
