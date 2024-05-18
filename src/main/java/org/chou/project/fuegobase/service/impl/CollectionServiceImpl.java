package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.model.database.Project;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
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
    private final ProjectRepository projectRepository;
    private final HashIdUtil hashIdUtil;


    @Autowired
    public CollectionServiceImpl(CollectionRepository collectionRepository, ProjectRepository projectRepository, HashIdUtil hashIdUtil) {
        this.collectionRepository = collectionRepository;
        this.projectRepository = projectRepository;
        this.hashIdUtil = hashIdUtil;

    }

    @Override
    public void createCollection(String projectId, CollectionData collectionData) {

        long id = hashIdUtil.decoded(projectId);

        Project project = projectRepository.findById(id).orElseThrow();
        if (isCollectionNameExistOrNot(id, collectionData.getName())) {
            throw new IllegalArgumentException("Name can not be repeated.");
        }
        Collection collection = new Collection();
        collection.setName(collectionData.getName());
        collection.setProjectId(id);

        collectionRepository.save(collection);


    }

    @Override
    public List<Collection> getCollections(String projectId) throws IllegalArgumentException {
        long id = hashIdUtil.decoded(projectId);
        if (id > 0) {
            return collectionRepository.getCollectionsByProjectId(id);
        } else {
            log.error("decode hash Id error");
            throw new IllegalArgumentException("Collection not found.");
        }
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
            throw new NoSuchElementException("Collection not found.");
        }
        return existingCollection;

    }

    @Override
    public void deleteCollection(String projectId, String collectionId) {
        long cId = hashIdUtil.decoded(collectionId);

        findCollectionByProjectIdAndId(projectId, collectionId);
        collectionRepository.deleteById(cId);
        log.info("Delete collection by " + collectionId + " successfully!");

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


