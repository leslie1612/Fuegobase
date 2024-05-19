package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.DocumentData;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.model.database.Document;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.repository.database.DocumentRepository;
import org.chou.project.fuegobase.service.DocumentService;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final CollectionRepository collectionRepository;
    private final HashIdUtil hashIdUtil;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, CollectionRepository collectionRepository, HashIdUtil hashIdUtil) {
        this.documentRepository = documentRepository;
        this.collectionRepository = collectionRepository;
        this.hashIdUtil = hashIdUtil;
    }

    @Override
    public void createDocument(String projectId, String collectionId, DocumentData documentData) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);

        Collection c = findCollectionByProjectIdAndId(id, cId);
        if (documentRepository.existsByNameAndCollectionId(documentData.getName(), cId)) {
            throw new IllegalArgumentException("Name can not be repeated.");
        }

        Document document = new Document();
        document.setName(documentData.getName());
        document.setCollectionId(c.getId());

        documentRepository.save(document);
    }

    @Override
    public List<Document> getDocuments(String projectId, String collectionId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        Collection c = findCollectionByProjectIdAndId(id, cId);

        return documentRepository.findDocumentsByCollectionId(c.getId());
    }

    @Override
    public Document updateDocumentById(String projectId, String collectionId, String documentId, DocumentData updateDocument) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);

        Document existingDocument = findDocument(id, cId, dId);

        existingDocument.setName(updateDocument.getName());
        documentRepository.save(existingDocument);
        return existingDocument;

    }

    @Override
    public void deleteDocument(String projectId, String collectionId, String documentId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);

        findDocument(id, cId, dId);
        documentRepository.deleteById(dId);
    }


    public Collection findCollectionByProjectIdAndId(long projectId, long collectionId) {
        return collectionRepository.findByProjectIdAndId(projectId, collectionId).orElseThrow();
    }

    public Document findDocument(long projectId, long collectionId, long documentId) {

        return documentRepository.findDocumentByProjectIdAndCollectionAndId(
                projectId, collectionId, documentId).orElseThrow();
    }


}
