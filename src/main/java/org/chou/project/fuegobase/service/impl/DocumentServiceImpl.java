package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.data.database.DocumentData;
import org.chou.project.fuegobase.model.database.Document;
import org.chou.project.fuegobase.repository.database.DocumentRepository;
import org.chou.project.fuegobase.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public void createDocument(String APIKey,String projectId, String collectionName, DocumentData documentData) {

        // TODO 驗證 APIKEY

        try{
            Document document = new Document();
            document.setName(documentData.getName());

            Long collectionId = documentRepository.getCollectionId(Long.parseLong(projectId),collectionName);
            document.setCollectionId(collectionId);

            documentRepository.save(document);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
