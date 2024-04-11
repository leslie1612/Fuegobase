package org.chou.project.fuegobase.service.impl;

import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.repository.database.FieldRepository;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldServiceImpl implements FieldService {

    private FieldRepository fieldRepository;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository){
        this.fieldRepository = fieldRepository;
    }

    @Override
    public void createField(String APIKey, String projectId,
                            String collectionName, String documentName, FieldData fieldData)
    {
        // TODO 驗證 APIKEY

    }
}
