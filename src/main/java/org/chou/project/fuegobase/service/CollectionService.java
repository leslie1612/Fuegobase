package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.model.database.Collection;

import java.util.List;

public interface CollectionService {
    void createCollection(long projectId, CollectionData collectionData);

    List<Collection> getCollections(String projectId);

    Collection updateCollectionById(String projectId, String collectionID, CollectionData collectionData);

    void deleteCollection(String projectId, String collectionId);
}
