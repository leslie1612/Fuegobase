package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.FieldKeyData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.data.dto.FilterDocumentDto;
import org.chou.project.fuegobase.model.database.FieldKey;
import org.chou.project.fuegobase.model.database.FieldType;
import org.chou.project.fuegobase.model.database.FieldValue;
import org.chou.project.fuegobase.repository.database.*;
import org.chou.project.fuegobase.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FieldServiceImpl implements FieldService {

    private DocumentRepository documentRepository;
    private FieldTypeRepository fieldTypeRepository;
    private FieldKeyRepository fieldKeyRepository;
    private FieldValueRepository fieldValueRepository;


    @Autowired
    public FieldServiceImpl(DocumentRepository documentRepository,
                            FieldTypeRepository fieldTypeRepository,
                            FieldKeyRepository fieldKeyRepository,
                            FieldValueRepository fieldValueRepository) {
        this.documentRepository = documentRepository;
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldKeyRepository = fieldKeyRepository;
        this.fieldValueRepository = fieldValueRepository;
    }

    @Override
    @Transactional
    public void createField(String APIKey, String projectId,
                            String collectionId, String documentId, FieldData fieldData) {
        // TODO 驗證 APIKEY

        try {
//            long documentId = getDocumentId(projectId, collectionName, documentName);
            FieldType fieldKeyType = getType(fieldData.getType());

            FieldKey fieldKey = new FieldKey();
            fieldKey.setDocumentId(Long.parseLong(documentId));
            fieldKey.setFieldType(fieldKeyType);
            fieldKey.setName(fieldData.getKey());

            FieldKey savedFieldKey = fieldKeyRepository.save(fieldKey);

            List<ValueInfoData> valueInfoDataList = fieldData.getValueInfo();
            for (ValueInfoData valueInfoData : valueInfoDataList) {

                // if key type is string, number or boolean, value type is same as key type
                if (valueInfoData.getType() == null) {
                    valueInfoData.setType(fieldData.getType());
                }

                FieldType fieldValueType = getType(valueInfoData.getType());

                FieldValue fieldValue = new FieldValue();
                fieldValue.setFieldKey(savedFieldKey);
                fieldValue.setKeyName(valueInfoData.getKey());
                fieldValue.setValueName(valueInfoData.getValue());
                fieldValue.setFieldType(fieldValueType);

                fieldValueRepository.save(fieldValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FieldDto> getFields(String APIKey, String projectId, String collectionId, String documentId) {
//        long documentId = getDocumentId(projectId, collectionId, documentId);
        return mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(Long.parseLong(documentId)));

    }

    @Override
    public List<FilterDocumentDto> getFieldsByFilter(String APIKey, String projectId, String collectionName, String filter, String value, String type) {
        List<Long> documentIdList = fieldKeyRepository.getDocumentIdsByFilter(filter, value, type);

        List<FilterDocumentDto> result = new ArrayList<>();
        for (Long documentId : documentIdList) {
            FilterDocumentDto filterDocumentDto = new FilterDocumentDto();
            filterDocumentDto.setId(documentId);
            filterDocumentDto.setName(documentRepository.findNameById(documentId));
            filterDocumentDto.setFieldDtoList(mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(documentId)));

            result.add(filterDocumentDto);
        }

        return result;
    }

//    public long getDocumentId(String projectId, String collectionName, String documentName) {
//        return documentRepository.findDocumentId(Long.parseLong(projectId), collectionName, documentName);
//    }

    public FieldType getType(String type) {
        return fieldTypeRepository.findFieldTypeByTypeName(type);
    }

    public List<FieldDto> mapProjectionToDto(List<FieldProjection> fieldProjectionList) {

        List<FieldDto> fieldDtoList = addKeyInfo(fieldProjectionList);

        for (FieldProjection fieldProjection : fieldProjectionList) {

            ValueInfoData valueInfoData = new ValueInfoData();
            valueInfoData.setValue(fieldProjection.getValueName());
            valueInfoData.setValueId(fieldProjection.getValueId());

            fieldDtoList.forEach(fieldDto -> {

                if (fieldProjection.getId() == fieldDto.getId()) {

                    if (fieldProjection.getKeyType().equals("Array")) {

                        valueInfoData.setType(fieldProjection.getValueType());

                        if (fieldDto.getValueInfo() == null) {
                            fieldDto.setValueInfo(new ArrayList<>());
                        }
                        ((List<ValueInfoData>) fieldDto.getValueInfo()).add(valueInfoData);

                    } else if (fieldProjection.getKeyType().equals("Map")) {

                        valueInfoData.setKey(fieldProjection.getKeyName());
                        valueInfoData.setType(fieldProjection.getValueType());

                        if (fieldDto.getValueInfo() == null) {
                            fieldDto.setValueInfo(new ArrayList<>());
                        }
                        ((List<ValueInfoData>) fieldDto.getValueInfo()).add(valueInfoData);

                    } else {
                        valueInfoData.setType(fieldProjection.getKeyType());
                        fieldDto.setValueInfo(valueInfoData);
                    }

                }
            });
        }

        return fieldDtoList;

    }

    public List<FieldDto> addKeyInfo(List<FieldProjection> fieldProjectionList) {

        Map<String, List<FieldProjection>> groupedByProductId = fieldProjectionList.stream()
                .collect(Collectors.groupingBy(fp -> fp.getId() + "-" + fp.getName() + "-" + fp.getKeyType()));

        Set<String> keySet = groupedByProductId.keySet();

        ArrayList<FieldDto> fieldDtoList = new ArrayList<>();
        for (String ks : keySet) {
            FieldDto fieldDto = new FieldDto();

            String[] keyInfo = ks.split("-");
            fieldDto.setId(Long.parseLong(keyInfo[0]));
            fieldDto.setKey(keyInfo[1]);
            fieldDto.setType(keyInfo[2]);

            fieldDtoList.add(fieldDto);
        }
        return fieldDtoList;
    }

    @Transactional
    @Override
    public FieldDto renameField(String APIKey,
                                String projectId,
                                String collectionId,
                                String documentId,
                                String fieldId,
                                FieldKeyData updatedFieldKey) {
        FieldKey existingFieldKey = fieldKeyRepository.findById(Long.parseLong(fieldId)).orElseThrow();
        existingFieldKey.setName(updatedFieldKey.getName());
        fieldKeyRepository.save(existingFieldKey);

        List<FieldValue> fieldValueList = fieldValueRepository.findAllByFieldKey(existingFieldKey);
        FieldDto fieldDto = new FieldDto();
        fieldDto.setId(existingFieldKey.getId());
        fieldDto.setKey(existingFieldKey.getName());
        fieldDto.setType(existingFieldKey.getFieldType().getTypeName());

        for (FieldValue fieldValue : fieldValueList) {
            if (fieldDto.getValueInfo() == null) {
                fieldDto.setValueInfo(new ArrayList<>());
            }

            ValueInfoData valueInfoData = new ValueInfoData();
            valueInfoData.setValueId(fieldValue.getId());
            valueInfoData.setKey(fieldValue.getKeyName());
            valueInfoData.setType(fieldValue.getFieldType().getTypeName());
            valueInfoData.setValue(fieldValue.getValueName());

            ((List<ValueInfoData>) fieldDto.getValueInfo()).add(valueInfoData);
        }

        return fieldDto;
    }

    @Override
    public FieldDto postField(String APIKey, String projectId, String collectionId, String documentId, String fieldId, ValueInfoData valueInfoData) {
        // with id -> update , no id -> add new
        if (valueInfoData.getValueId() > 0) {
            return updateField(fieldId, valueInfoData);
        }else{
            return insertNewData(fieldId, valueInfoData);
        }
    }

    public FieldDto updateField(String fieldId, ValueInfoData valueInfoData) {
        FieldValue existingFieldValue = fieldValueRepository.findById(valueInfoData.getValueId()).orElseThrow();
        FieldKey fieldKey = fieldKeyRepository.findById(Long.parseLong(fieldId)).orElseThrow();

        if (fieldKey.getFieldType().getTypeName().equals("Map")) {
            existingFieldValue.setKeyName(valueInfoData.getKey());
        }
        existingFieldValue.setValueName(valueInfoData.getValue());
        fieldValueRepository.save(existingFieldValue);

        FieldDto fieldDto = new FieldDto();
        fieldDto.setId(fieldKey.getId());
        fieldDto.setKey(fieldKey.getName());
        fieldDto.setType(fieldKey.getFieldType().getTypeName());
        fieldDto.setValueInfo(existingFieldValue);

        return mapFieldKeyAndValueToFieldDto(fieldKey, existingFieldValue);
    }

    public FieldDto insertNewData(String fieldId, ValueInfoData valueInfoData) {
        FieldKey fieldKey = fieldKeyRepository.findById(Long.parseLong(fieldId)).orElseThrow();

        FieldValue fieldValue = new FieldValue();
        fieldValue.setFieldKey(fieldKey);
        fieldValue.setKeyName(valueInfoData.getKey());
        fieldValue.setValueName(valueInfoData.getValue());
        fieldValue.setFieldType(fieldTypeRepository.findFieldTypeByTypeName(valueInfoData.getType()));

        fieldValueRepository.save(fieldValue);

        return mapFieldKeyAndValueToFieldDto(fieldKey, fieldValue);

    }

    public FieldDto mapFieldKeyAndValueToFieldDto(FieldKey fieldKey, FieldValue fieldValue) {

        FieldDto fieldDto = new FieldDto();
        fieldDto.setId(fieldKey.getId());
        fieldDto.setKey(fieldKey.getName());
        fieldDto.setType(fieldKey.getFieldType().getTypeName());

        ValueInfoData valueInfoData = new ValueInfoData();
        valueInfoData.setValueId(fieldValue.getId());
        valueInfoData.setKey(fieldValue.getKeyName());
        valueInfoData.setValue(fieldValue.getValueName());
        valueInfoData.setType(fieldValue.getFieldType().getTypeName());

        fieldDto.setValueInfo(valueInfoData);

        return fieldDto;
    }


}
