package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.model.database.Collection;

import java.util.List;

public interface CollectionService {
    void createCollection(String APIKey, String projectId, CollectionData collectionData);

    List<Collection> getCollections(String APIKey, String projectId);

//    CollectionDto getDocumentsByCollectionId(String APIKey, String projectId, String collectionId);

}
