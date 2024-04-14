package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.data.database.CollectionData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    @Autowired
    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    public void createCollection(String APIKey, String projectId, CollectionData collectionData) {

        // TODO 驗證 APIKEY
        // 1. 用 projectId 找到對應的 API Key
        // 2. 比較資料庫裡面的和 request 帶的一不一樣
        // 3. spring boot security

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
        Collection existingCollection = collectionRepository.findById(Long.parseLong(collectionId)).orElseThrow();
        existingCollection.setName(updatedCollection.getName());
        collectionRepository.save(existingCollection);

        return existingCollection;
    }

}
