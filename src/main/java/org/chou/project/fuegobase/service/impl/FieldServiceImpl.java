package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.model.database.Field;
import org.chou.project.fuegobase.repository.database.FieldRepository;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FieldServiceImpl implements FieldService {

    private FieldRepository fieldRepository;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    @Override
    public void createField(String APIKey, String projectId,
                            String collectionName, String documentName, FieldData fieldData) {
        // TODO 驗證 APIKEY

        try {
            Long documentId = getDocumentId(projectId, collectionName, documentName);
            int keyTypeId = getTypeId(fieldData.getType());

            List<ValueInfoData> valueInfoDataList = fieldData.getValueInfo();
            for (ValueInfoData valueInfoData : valueInfoDataList) {

                if(valueInfoData.getType() == null){
                    valueInfoData.setType(fieldData.getType());
                }

                int valueTypeId = getTypeId(valueInfoData.getType());

                Field field = new Field();
                field.setDocumentId(documentId);
                field.setKeyTypeId(keyTypeId);

                if (keyTypeId == 5) {
                    field.setKeyName(fieldData.getKey() + "." + valueInfoData.getKey());
                } else {
                    field.setKeyName(fieldData.getKey());
                }

                field.setValueTypeId(valueTypeId);
                field.setValueName(valueInfoData.getValue());

                fieldRepository.save(field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getDocumentId(String projectId, String collectionName, String documentName) {
        return fieldRepository.getDocumentId(Long.parseLong(projectId), collectionName, documentName);
    }

    public int getTypeId(String type) {

        return switch (type) {
            case ("String") -> 1;
            case ("Number") -> 2;
            case ("Boolean") -> 3;
            case ("Array") -> 4;
            case ("Map") -> 5;
            default -> -1;
        };
    }

    public void storeData() {

    }

}
