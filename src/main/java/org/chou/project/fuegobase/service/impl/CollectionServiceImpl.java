package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public CollectionServiceImpl(CollectionRepository collectionRepository,ProjectRepository projectRepository) {
        this.collectionRepository = collectionRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void createCollection(String APIKey, String projectId, CollectionData collectionData) {

        // TODO 驗證 APIKEY
        // 1. 用 projectId 找到對應的 API Key
        // 2. 比較資料庫裡面的和 request 帶的一不一樣
        // 3. spring boot security

        Project project = projectRepository.findById(Long.parseLong(projectId)).orElseThrow();

        Collection collection = new Collection();

        collection.setName(collectionData.getName());
        collection.setProjectId(Long.parseLong(projectId));

        collectionRepository.save(collection);

    }

    @Override
    public List<Collection> getCollections(String APIKey, String projectId) {
        return collectionRepository.getCollectionsByProjectId(Long.parseLong(projectId));
    }

    @Override
    public Collection updateCollectionById(String APIKey,
                                           String projectId,
                                           String collectionId,
                                           CollectionData updatedCollection) {

//        Collection existingCollection = null;
//        try {
//            existingCollection = findCollectionByProjectIdAndId(projectId, collectionId);
//            existingCollection.setName(updatedCollection.getName());
//            collectionRepository.save(existingCollection);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return existingCollection;
        Collection existingCollection = findCollectionByProjectIdAndId(projectId, collectionId);
        if (existingCollection != null) {
            existingCollection.setName(updatedCollection.getName());
            collectionRepository.save(existingCollection);
        } else {
            throw new NoSuchElementException("Collection not found.");
        }
        return existingCollection;

    }

    @Override
    public void deleteCollection(String APIKey, String projectId, String collectionId) {
//        findCollectionByProjectIdAndId(projectId, collectionId);
//        collectionRepository.deleteById(Long.parseLong(collectionId));
//        log.info("Delete collection by " + collectionId + " successfully!");
//
//    }

//        if (findCollectionByProjectIdAndId(projectId, collectionId) != null) {
//            collectionRepository.deleteById(Long.parseLong(collectionId));
//            log.info("Delete collection by " + collectionId + " successfully!");
//        } else {
//            throw new NoSuchElementException("Collection not found.");
//        }
        findCollectionByProjectIdAndId(projectId, collectionId);
        collectionRepository.deleteById(Long.parseLong(collectionId));
        log.info("Delete collection by " + collectionId + " successfully!");

    }


//public Collection findCollectionByProjectIdAndId(String projectId, String collectionId) throws NoSuchElementException{
//    return Optional.ofNullable(collectionRepository.findByProjectIdAndId(Long.parseLong(projectId), Long.parseLong(collectionId))).orElseThrow();
//}


    public Collection findCollectionByProjectIdAndId(String projectId, String collectionId) {
        return collectionRepository.findByProjectIdAndId(Long.parseLong(projectId), Long.parseLong(collectionId)).orElseThrow();
    }
}


