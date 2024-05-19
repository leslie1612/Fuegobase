package org.chou.project.fuegobase.controller.database;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.exception.ResourceNotFoundException;
import org.chou.project.fuegobase.service.CollectionService;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/collections")
public class CollectionController {
    private final CollectionService collectionService;
    private final FieldService fieldService;

    @Autowired
    public CollectionController(CollectionService collectionService,
                                FieldService fieldService) {
        this.collectionService = collectionService;
        this.fieldService = fieldService;
    }

    @PostMapping
    public ResponseEntity<?> createCollection(@PathVariable String projectId,
                                              @RequestBody CollectionData collectionData) {

        try {
            collectionService.createCollection(projectId, collectionData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }

    @GetMapping
    public ResponseEntity<?> getCollections(@PathVariable String projectId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(collectionService.getCollections(projectId)));
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<?> getFieldsByFilter(@PathVariable String projectId, @PathVariable String collectionId,
                                               @RequestParam String filter, @RequestParam String value,
                                               @RequestParam String type, @RequestParam String operator) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.getFieldsByFilter(projectId, collectionId, filter, value, type, operator)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Data Not Found."));
        }

    }

    @PatchMapping("/{collectionId}")
    public ResponseEntity<?> renameCollection(@PathVariable String projectId,
                                              @PathVariable String collectionId,
                                              @RequestBody CollectionData updatedCollection) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new GenericResponse<>(collectionService.updateCollectionById(projectId, collectionId, updatedCollection)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<?> deleteCollection(@PathVariable String projectId, @PathVariable String collectionId) {

        try {
            collectionService.deleteCollection(projectId, collectionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

}
