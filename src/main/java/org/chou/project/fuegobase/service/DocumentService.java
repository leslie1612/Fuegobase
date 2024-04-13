package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.DocumentData;
import org.chou.project.fuegobase.model.database.Document;

import java.util.List;

public interface DocumentService {
    void createDocument(String APIKey,String projectId, String collectionName, DocumentData documentData);
    List<Document> getDocuments(String APIKey, String projectId, String collectionName);
}
