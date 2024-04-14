package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.data.database.DocumentData;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.model.database.Document;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.repository.database.DocumentRepository;
import org.chou.project.fuegobase.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final CollectionRepository collectionRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, CollectionRepository collectionRepository) {
        this.documentRepository = documentRepository;
        this.collectionRepository = collectionRepository;
    }

    @Override
    public void createDocument(String APIKey, String projectId, String collectionId, DocumentData documentData) {

        // TODO 驗證 APIKEY

        try {
//            long collectionId = getCollectionId(projectId, collectionName);

            Document document = new Document();
            document.setName(documentData.getName());
            document.setCollectionId(Long.parseLong(collectionId));

            documentRepository.save(document);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Document> getDocuments(String APIKey, String projectId, String collectionId) {

//        long collectionId  = getCollectionId(projectId, collectionName);
        return documentRepository.findDocumentsByCollectionId(Long.parseLong(collectionId));
    }

    @Override
    public Document updateDocumentById(String APIKey,
                                             String projectId,
                                             String CollectionID,
                                             String documentId,
                                             DocumentData updateDocument) {
        Document existingDocument = documentRepository.findById(Long.parseLong(documentId)).orElseThrow();
        existingDocument.setName(updateDocument.getName());
        documentRepository.save(existingDocument);
        return existingDocument;
    }

    //    public Collection getCollection(String projectId, String collectionName) {
//        return collectionRepository.findCollection(Long.parseLong(projectId), collectionName);
//    }
    public long getCollectionId(String projectId, String collectionName) {
        return collectionRepository.findCollectionId(Long.parseLong(projectId), collectionName);
    }

}
