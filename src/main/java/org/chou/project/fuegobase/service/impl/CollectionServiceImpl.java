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
    public CollectionServiceImpl(CollectionRepository collectionRepository, ProjectRepository projectRepository) {
        this.collectionRepository = collectionRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public void createCollection(String APIKey, long projectId, CollectionData collectionData) {

        Project project = projectRepository.findById(projectId).orElseThrow();
        if (isCollectionNameExistOrNot(projectId, collectionData.getName())) {
            throw new IllegalArgumentException("Name can not be repeated.");
        }
        Collection collection = new Collection();
        collection.setName(collectionData.getName());
        collection.setProjectId(projectId);

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

        findCollectionByProjectIdAndId(projectId, collectionId);
        collectionRepository.deleteById(Long.parseLong(collectionId));
        log.info("Delete collection by " + collectionId + " successfully!");

    }


    public Collection findCollectionByProjectIdAndId(String projectId, String collectionId) {
        return collectionRepository.findByProjectIdAndId(Long.parseLong(projectId), Long.parseLong(collectionId)).orElseThrow();
    }

    public Boolean isCollectionNameExistOrNot(long projectId, String collectionName) {
        return collectionRepository.existsByProjectIdAndName(projectId, collectionName);
    }
}


