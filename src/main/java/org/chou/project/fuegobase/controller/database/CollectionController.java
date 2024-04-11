package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectID}/collections")
public class CollectionController {

    private final CollectionService collectionService;

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping
    public void createProject(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                              @PathVariable String projectID,
                              @RequestBody CollectionData collectionData) {
//        String APIKey = authorization.split(" ")[1].trim();
        String APIKey = "aaa12345bbb";
        collectionService.createCollection(APIKey, projectID, collectionData);
    }
}
