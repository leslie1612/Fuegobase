package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.CollectionData;

public interface CollectionService {
    void createCollection(String APIKey,String projectId, CollectionData collectionData);
}
