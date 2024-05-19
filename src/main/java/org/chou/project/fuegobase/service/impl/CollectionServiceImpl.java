package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.exception.ResourceNotFoundException;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.service.CollectionService;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final HashIdUtil hashIdUtil;


    @Autowired
    public CollectionServiceImpl(CollectionRepository collectionRepository, HashIdUtil hashIdUtil) {
        this.collectionRepository = collectionRepository;
        this.hashIdUtil = hashIdUtil;

    }

    @Override
    public void createCollection(String projectId, CollectionData collectionData) {

        long id = hashIdUtil.decoded(projectId);

        if (isCollectionNameExistOrNot(id, collectionData.getName())) {
            throw new IllegalArgumentException("Name can not be repeated.");
        }
        Collection collection = new Collection();
        collection.setName(collectionData.getName());
        collection.setProjectId(id);

        collectionRepository.save(collection);
    }

    @Override
    public List<Collection> getCollections(String projectId) {
        long id = hashIdUtil.decoded(projectId);
        return collectionRepository.getCollectionsByProjectId(id);
    }

    @Override
    public Collection updateCollectionById(String projectId,
                                           String collectionId,
                                           CollectionData updatedCollection) {
        Collection existingCollection = findCollectionByProjectIdAndId(projectId, collectionId);
        if (existingCollection != null) {
            existingCollection.setName(updatedCollection.getName());
            collectionRepository.save(existingCollection);
        } else {
            throw new ResourceNotFoundException("Collection not found.");
        }
        return existingCollection;
    }

    @Override
    public void deleteCollection(String projectId, String collectionId) {
        long cId = hashIdUtil.decoded(collectionId);
        try {
            findCollectionByProjectIdAndId(projectId, collectionId);
            collectionRepository.deleteById(cId);
        } catch (NoSuchElementException e) {
            throw new ResourceNotFoundException("Collection not found.");
        }
    }


    public Collection findCollectionByProjectIdAndId(String projectId, String collectionId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        return collectionRepository.findByProjectIdAndId(id, cId).orElseThrow();
    }

    public Boolean isCollectionNameExistOrNot(long projectId, String collectionName) {
        return collectionRepository.existsByProjectIdAndName(projectId, collectionName);
    }
}


