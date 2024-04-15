package org.chou.project.fuegobase.controller.database;

import jakarta.websocket.server.PathParam;
import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.FieldKeyData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/collections/{collectionId}/documents/{documentId}/fields")
public class FieldController {
    private FieldService fieldService;
    private String API_KEY = "aaa12345bbb";

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping
    public ResponseEntity<?> createField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                         @PathVariable String projectId,
                                         @PathVariable String collectionId,
                                         @PathVariable String documentId,
                                         @RequestBody FieldData fieldData) {
        try {
            fieldService.createField(API_KEY, projectId, collectionId, documentId, fieldData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Field not found");
        }

//        String APIKey = authorization.split(" ")[1].trim();

    }

    @GetMapping
    public ResponseEntity<?> getFields(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                       @PathVariable String projectId,
                                       @PathVariable String collectionId,
                                       @PathVariable String documentId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.getFields(API_KEY, projectId, collectionId, documentId)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Field not found");
        }
    }

    // update field info
//    @PatchMapping("/{fieldId}")
//    public ResponseEntity<?> renameField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
//                                         @PathVariable String projectId,
//                                         @PathVariable String collectionId,
//                                         @PathVariable String documentId,
//                                         @PathVariable String fieldId,
//                                         @RequestBody FieldKeyData updateKeyData) {
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new GenericResponse<>(fieldService.renameField(API_KEY, projectId, collectionId, documentId, fieldId, updateKeyData)));
//    }

    @PatchMapping("/{fieldId}")
    public ResponseEntity<?> updateField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                         @PathVariable String projectId,
                                         @PathVariable String collectionId,
                                         @PathVariable String documentId,
                                         @PathVariable String fieldId,
                                         @RequestBody ValueInfoData valueInfoData) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.updateField(API_KEY, projectId, collectionId, documentId, fieldId, valueInfoData)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Field not found");
        }
    }

    @PostMapping("/{fieldId}")
    public ResponseEntity<?> addFieldValue(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                           @PathVariable String projectId,
                                           @PathVariable String collectionId,
                                           @PathVariable String documentId,
                                           @PathVariable String fieldId,
                                           @RequestBody ValueInfoData valueInfoData,
                                           @RequestParam(required = true) String valueId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.addFieldValue(API_KEY, projectId, collectionId, documentId, fieldId, valueId, valueInfoData)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Field value not found");
        }

    }


    @DeleteMapping("/{fieldId}")
    public ResponseEntity<?> deleteField(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                         @PathVariable String projectId,
                                         @PathVariable String collectionId,
                                         @PathVariable String documentId,
                                         @PathVariable String fieldId,
                                         @RequestParam(required = false) String valueId) {
        try {
            fieldService.deleteField(API_KEY, projectId, collectionId, documentId, fieldId, valueId);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Field not found");
        }

    }
}
