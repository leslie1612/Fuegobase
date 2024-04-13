package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/collections")
public class CollectionController {
    private final CollectionService collectionService;
    private String API_KEY = "aaa12345bbb";

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                           @PathVariable String projectId,
                                           @RequestBody CollectionData collectionData) {
//        String APIKey = authorization.split(" ")[1].trim();
        collectionService.createCollection(API_KEY, projectId, collectionData);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<?> getCollections(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                            @PathVariable String projectId) {

//        if (collectionId != null) {
//            return ResponseEntity
//                    .status(HttpStatus.OK)
//                    .body(new GenericResponse<>(collectionService.getDocumentsByCollectionId(APIKey, projectId,collectionId)));
//        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new GenericResponse<>(collectionService.getCollections(API_KEY, projectId)));
    }
}
