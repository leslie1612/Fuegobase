package org.chou.project.fuegobase.controller.database;

import org.chou.project.fuegobase.data.GenericResponse;
import org.chou.project.fuegobase.data.database.DocumentData;
import org.chou.project.fuegobase.error.ErrorResponse;
import org.chou.project.fuegobase.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/databases/projects/{projectId}/collections/{collectionId}/documents")
public class DocumentController {
    private DocumentService documentService;
    private String API_KEY = "aaa12345bbb";

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<?> createDocument(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                            @PathVariable String projectId,
                                            @PathVariable String collectionId,
                                            @RequestBody DocumentData documentData) {

//        String APIKey = authorization.split(" ")[1].trim();
        try {
            documentService.createDocument(API_KEY, projectId, collectionId, documentData);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Document not found"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }

    }

    @GetMapping
    public ResponseEntity<?> getDocuments(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                          @PathVariable String projectId,
                                          @PathVariable String collectionId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(documentService.getDocuments(API_KEY, projectId, collectionId)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Document not found"));
        }

    }

    @PatchMapping("/{documentId}")
    public ResponseEntity<?> renameDocument(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                            @PathVariable String projectId,
                                            @PathVariable String collectionId,
                                            @PathVariable String documentId,
                                            @RequestBody DocumentData updatedDocument) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse<>(
                            documentService.updateDocumentById(API_KEY, projectId, collectionId, documentId, updatedDocument))
                    );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Document not found"));
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<?> deleteDocument(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                            @PathVariable String projectId,
                                            @PathVariable String collectionId,
                                            @PathVariable String documentId) {
        try {
            documentService.deleteDocument(API_KEY, projectId, collectionId, documentId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Document not found"));
        }

    }
}
