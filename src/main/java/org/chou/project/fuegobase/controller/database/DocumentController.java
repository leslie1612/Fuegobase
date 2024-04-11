package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.database.DocumentData;
import org.chou.project.fuegobase.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/{collectionName}/documents")
public class DocumentController {

    private DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public void createDocument(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                               @PathVariable String projectId,
                               @PathVariable String collectionName,
                               @RequestBody DocumentData documentData) {

//        String APIKey = authorization.split(" ")[1].trim();
        String APIKey = "aaa12345bbb";

        documentService.createDocument(APIKey,projectId,collectionName,documentData);
    }

}
