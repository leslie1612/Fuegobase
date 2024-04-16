package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.service.CollectionService;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/collections")
public class CollectionController {
    private final CollectionService collectionService;
    private final FieldService fieldService;
    private String API_KEY = "aaa12345bbb";

    @Autowired
    public CollectionController(CollectionService collectionService, FieldService fieldService) {
        this.collectionService = collectionService;
        this.fieldService = fieldService;
    }

    @PostMapping
    public ResponseEntity<?> createCollection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                              @PathVariable String projectId,
                                              @RequestBody CollectionData collectionData) {
//        String APIKey = authorization.split(" ")[1].trim();
        try{
            collectionService.createCollection(API_KEY, projectId, collectionData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project is not exist.");
        }

    }

    @GetMapping
    public ResponseEntity<?> getCollections(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                            @PathVariable String projectId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(collectionService.getCollections(API_KEY, projectId)));
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<?> getFieldsByFilter(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                               @PathVariable String projectId,
                                               @PathVariable String collectionId,
                                               @RequestParam String filter,
                                               @RequestParam String value,
                                               @RequestParam String type) {
//        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(fieldService.getFieldsByFilter(API_KEY, projectId, collectionId, filter, value, type)));
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }

    }

    @PatchMapping("/{collectionId}")
    public ResponseEntity<?> renameCollection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                              @PathVariable String projectId,
                                              @PathVariable String collectionId,
                                              @RequestBody CollectionData updatedCollection) {
        try {
            Collection c = collectionService.updateCollectionById(API_KEY, projectId, collectionId, updatedCollection);
            return ResponseEntity.status(HttpStatus.OK).body(new GenericResponse<>(c));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @DeleteMapping("/{collectionId}")
    public ResponseEntity<?> deleteCollection(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                              @PathVariable String projectId,
                                              @PathVariable String collectionId) {

        try {
            collectionService.deleteCollection(API_KEY, projectId, collectionId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection not found");
        }
    }

}
