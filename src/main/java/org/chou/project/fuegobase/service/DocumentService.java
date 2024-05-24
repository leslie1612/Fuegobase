package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.DocumentData;
import org.chou.project.fuegobase.model.database.Document;

import java.util.List;

public interface DocumentService {
    void createDocument(String projectId, String collectionId, DocumentData documentData);

    List<Document> getDocuments(String projectId, String collectionId);

    Document updateDocumentById(String projectId, String CollectionID, String documentId, DocumentData updatedDocument);

    void deleteDocument(String projectId, String collectionId, String documentId);
}
