package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.FieldKeyData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.data.dto.FilterDocumentDto;
import org.chou.project.fuegobase.model.database.Document;
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


        Document document = findDocumentByProjectIdAndCollectionAndId(projectId, collectionId, documentId);
        FieldType fieldKeyType = getType(fieldData.getType());

        FieldKey fieldKey = new FieldKey();
        fieldKey.setDocumentId(document.getId());
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
            addWriteNumber(1);

        }

    }

    @Override
    public List<FieldDto> getFields(String APIKey, String projectId, String collectionId, String documentId) {
        Document document = findDocumentByProjectIdAndCollectionAndId(projectId, collectionId, documentId);
        List<FieldDto> fieldDtoList = mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(document.getId()));
        addReadNumber(fieldDtoList.size());
        return fieldDtoList;

    }

    @Override
    public List<FilterDocumentDto> getFieldsByFilter(String APIKey, String projectId, String collectionId, String filter, String value, String type) {
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
                .collect(Collectors.groupingBy(fp -> fp.getId() + "-" + fp.getDocumentId() + "-" + fp.getName() + "-" + fp.getKeyType()));

        Set<String> keySet = groupedByProductId.keySet();

        ArrayList<FieldDto> fieldDtoList = new ArrayList<>();
        for (String ks : keySet) {
            FieldDto fieldDto = new FieldDto();

            String[] keyInfo = ks.split("-");
            fieldDto.setId(Long.parseLong(keyInfo[0]));
            fieldDto.setDocumentId(Long.parseLong(keyInfo[1]));
            fieldDto.setKey(keyInfo[2]);
            fieldDto.setType(keyInfo[3]);

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
//        FieldKey existingFieldKey = fieldKeyRepository.findById(Long.parseLong(fieldId)).orElseThrow();
        FieldKey existingFieldKey = findFieldKey(projectId, collectionId, documentId, fieldId);

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

        addReadNumber(1);
        addWriteNumber(1);
        return fieldDto;
    }

    @Override
    public void deleteField(String APIKey, String projectId, String collectionId, String documentId, String fieldId, String valueId) {

        if (valueId != null) {
            findFieldValue(projectId, collectionId, documentId, fieldId, valueId);
            fieldValueRepository.deleteById(Long.parseLong(valueId));
            log.info("Delete value by " + valueId + " successfully!");
        } else {
            findFieldKey(projectId, collectionId, documentId, fieldId);
            fieldKeyRepository.deleteById(Long.parseLong(fieldId));
            log.info("Delete field by " + fieldId + " successfully!");
        }
        addReadNumber(1);
        addWriteNumber(1);
    }

    @Override
    public FieldDto updateField(String APIKey, String projectId, String collectionId, String documentId, String fieldId, ValueInfoData valueInfoData) {
        FieldValue existingFieldValue = findFieldValue(projectId, collectionId, documentId, fieldId, String.valueOf(valueInfoData.getValueId()));
//        FieldValue existingFieldValue = fieldValueRepository.findById(valueInfoData.getValueId()).orElseThrow();

        FieldKey fieldKey = fieldKeyRepository.findById(Long.parseLong(fieldId)).orElseThrow();

        if (fieldKey.getFieldType().getTypeName().equals("Map")) {
            existingFieldValue.setKeyName(valueInfoData.getKey());
        }
        existingFieldValue.setValueName(valueInfoData.getValue());
        fieldValueRepository.save(existingFieldValue);

        addReadNumber(1);
        addWriteNumber(1);

        return mapFieldKeyAndValueToFieldDto(fieldKey, existingFieldValue);
    }

    @Override
    public FieldDto addFieldValue(String APIKey, String projectId, String collectionId, String documentId, String fieldId, String valueId, ValueInfoData valueInfoData) {
        FieldKey fieldKey = findFieldKey(projectId, collectionId, documentId, fieldId);

        FieldValue fieldValue = new FieldValue();
        fieldValue.setFieldKey(fieldKey);
        fieldValue.setKeyName(valueInfoData.getKey());
        fieldValue.setValueName(valueInfoData.getValue());
        fieldValue.setFieldType(fieldTypeRepository.findFieldTypeByTypeName(valueInfoData.getType()));

        fieldValueRepository.save(fieldValue);

        addReadNumber(1);
        addWriteNumber(1);

        return mapFieldKeyAndValueToFieldDto(fieldKey, fieldValue);
    }

    public FieldDto mapFieldKeyAndValueToFieldDto(FieldKey fieldKey, FieldValue fieldValue) {

        try {
            FieldDto fieldDto = new FieldDto();
            fieldDto.setId(fieldKey.getId());
            fieldDto.setDocumentId(fieldKey.getDocumentId());
            fieldDto.setKey(fieldKey.getName());
            fieldDto.setType(fieldKey.getFieldType().getTypeName());

            ValueInfoData valueInfoData = new ValueInfoData();
            valueInfoData.setValueId(fieldValue.getId());
            valueInfoData.setKey(fieldValue.getKeyName());
            valueInfoData.setValue(fieldValue.getValueName());
            valueInfoData.setType(fieldValue.getFieldType().getTypeName());

            fieldDto.setValueInfo(valueInfoData);
            return fieldDto;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public Document findDocumentByProjectIdAndCollectionAndId(String projectId, String collectionId, String documentId) {
        return documentRepository.findDocumentByProjectIdAndCollectionAndId(
                Long.parseLong(projectId), Long.parseLong(collectionId), Long.parseLong(documentId)).orElseThrow();

    }

    public FieldKey findFieldKey(String projectId, String collectionId, String documentId, String fieldId) {
        int count = fieldKeyRepository.isFieldKeyExist(
                Long.parseLong(projectId),
                Long.parseLong(collectionId),
                Long.parseLong(documentId),
                Long.parseLong(fieldId)
        );
        if (count > 0) {
            return fieldKeyRepository.findById(Long.parseLong(fieldId)).orElse(null);
        } else {
            throw new NoSuchElementException();
        }
    }

    public FieldValue findFieldValue(String projectId, String collectionId, String documentId, String fieldId, String valueId) {
        int count = fieldValueRepository.isFieldValueExist(
                Long.parseLong(projectId),
                Long.parseLong(collectionId),
                Long.parseLong(documentId),
                Long.parseLong(fieldId),
                Long.parseLong(valueId)
        );
        if (count > 0) {
            return fieldValueRepository.findById(Long.parseLong(valueId)).orElse(null);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void addReadNumber(int times) {
        log.info("read:" + times);
    }

    public void addWriteNumber(int times) {
        log.info("write:" + times);
    }

}
