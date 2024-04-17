package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

        Collection c = findCollectionByProjectIdAndId(projectId, collectionId);
        if (documentRepository.existsByName(documentData.getName())) {
            throw new IllegalArgumentException("Name can not be repeated.");
        }

        Document document = new Document();
        document.setName(documentData.getName());
        document.setCollectionId(c.getId());

        documentRepository.save(document);
    }

    @Override
    public List<Document> getDocuments(String APIKey, String projectId, String collectionId) {
        Collection c = findCollectionByProjectIdAndId(projectId, collectionId);
        return documentRepository.findDocumentsByCollectionId(c.getId());
    }

    @Override
    public Document updateDocumentById(String APIKey,
                                       String projectId,
                                       String collectionId,
                                       String documentId,
                                       DocumentData updateDocument) {

        Document existingDocument = findDocument(projectId, collectionId, documentId);
        existingDocument.setName(updateDocument.getName());
        documentRepository.save(existingDocument);
        return existingDocument;
    }

    @Override
    public void deleteDocument(String APIKey, String projectId, String collectionId, String documentId) {
        findDocument(projectId, collectionId, documentId);
        documentRepository.deleteById(Long.parseLong(documentId));
        log.info("Delete document by : " + documentId + " successfully!");
    }


    public Collection findCollectionByProjectIdAndId(String projectId, String collectionId) {
        return collectionRepository.findByProjectIdAndId(Long.parseLong(projectId), Long.parseLong(collectionId)).orElseThrow();
    }

    public Document findDocument(String projectId, String collectionId, String documentId) {
        return documentRepository.findDocumentByProjectIdAndCollectionAndId(
                Long.parseLong(projectId),
                Long.parseLong(collectionId),
                Long.parseLong(documentId)).orElseThrow();
    }


}
