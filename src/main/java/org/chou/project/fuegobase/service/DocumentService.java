package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.DocumentData;

public interface DocumentService {

    void createDocument(String APIKey,String projectId, String collectionName, DocumentData documentData);
}
