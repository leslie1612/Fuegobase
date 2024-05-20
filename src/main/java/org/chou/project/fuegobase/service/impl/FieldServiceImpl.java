package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.data.dto.FilterDocumentDto;
import org.chou.project.fuegobase.model.database.Document;
import org.chou.project.fuegobase.model.database.FieldKey;
import org.chou.project.fuegobase.model.database.FieldType;
import org.chou.project.fuegobase.model.database.FieldValue;
import org.chou.project.fuegobase.repository.database.*;
import org.chou.project.fuegobase.service.FieldService;
import org.chou.project.fuegobase.service.RedisService;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FieldServiceImpl implements FieldService {
    private final CollectionRepository collectionRepository;
    private final DocumentRepository documentRepository;
    private final FieldTypeRepository fieldTypeRepository;
    private final FieldKeyRepository fieldKeyRepository;
    private final FieldValueRepository fieldValueRepository;
    private final HashIdUtil hashIdUtil;
    private final RedisService redisService;

    @Autowired
    public FieldServiceImpl(CollectionRepository collectionRepository, DocumentRepository documentRepository,
                            FieldTypeRepository fieldTypeRepository, FieldKeyRepository fieldKeyRepository,
                            FieldValueRepository fieldValueRepository, HashIdUtil hashIdUtil, RedisService redisService) {
        this.collectionRepository = collectionRepository;
        this.documentRepository = documentRepository;
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldKeyRepository = fieldKeyRepository;
        this.fieldValueRepository = fieldValueRepository;
        this.hashIdUtil = hashIdUtil;
        this.redisService = redisService;
    }

    @Override
    public void createField(String projectId, String collectionId,
                            String documentId, FieldData fieldData) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);

        Document document = findDocumentByProjectIdAndCollectionAndId(id, cId, dId);
        FieldType fieldKeyType = stringToType(fieldData.getType());
        if (fieldKeyRepository.existsByNameAndDocumentId(fieldData.getKey(), dId)) {
            throw new IllegalArgumentException("Key can not be repeated.");
        }

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

            FieldType fieldValueType = stringToType(valueInfoData.getType());
            FieldValue fieldValue = new FieldValue();
            fieldValue.setFieldKey(savedFieldKey);
            fieldValue.setKeyName(valueInfoData.getKey());
            fieldValue.setValueName(valueInfoData.getValue());
            fieldValue.setFieldType(fieldValueType);

            fieldValueRepository.save(fieldValue);
            redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(savedFieldKey.getId()), "write");

        }
    }

    @Override
    public List<FieldDto> getFields(String projectId, String collectionId, String documentId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);

        Document document = findDocumentByProjectIdAndCollectionAndId(id, cId, dId);
        List<FieldDto> fieldDtoList = mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(document.getId()));

        for (FieldDto fieldDto : fieldDtoList) {
            redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fieldDto.getId()), "read");
        }
        return fieldDtoList;
    }

    @Override
    public List<FilterDocumentDto> getFieldsByFilter(String projectId, String collectionId, String filter,
                                                     String value, String valueType, String operator) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);

        collectionRepository.findByProjectIdAndId(id, cId).orElseThrow();
        String[] keys = filter.split("\\.");
        List<Document> documents;

        if (operator.equals("CONTAINS") && keys.length != 1) {
            throw new IllegalArgumentException();
        }

        if (operator.equals("CONTAINS")) {
            documents = fieldKeyRepository.getDocumentsByArrayFilter(cId, keys[0], value, valueType);
        } else {
            if (keys.length > 1) {
                String fieldKey = keys[0];
                String valueKey = keys[1];
                if (valueType.equals("Number")) {
                    documents = fieldKeyRepository.getDocumentsByMapFilterWithNumber(cId, fieldKey, valueKey,
                            value, valueType, operator);
                } else {
                    documents = fieldKeyRepository.getDocumentsByMapFilter(cId, fieldKey, valueKey,
                            value, valueType, operator);
                }
            } else {
                if (valueType.equals("Number")) {
                    documents = fieldKeyRepository.getDocumentsByFilterWithNumber(cId, keys[0], value, valueType, operator);
                } else {
                    documents = fieldKeyRepository.getDocumentsByFilter(cId, keys[0], value, valueType, operator);
                }
            }
        }

        List<FilterDocumentDto> result = new ArrayList<>();
        for (Document document : documents) {
            FilterDocumentDto filterDocumentDto = new FilterDocumentDto();

            filterDocumentDto.setHashId(document.getHashId());
            filterDocumentDto.setName(document.getName());
            filterDocumentDto.setFieldDtoList(mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(document.getId())));

            for (FieldDto fieldDto : filterDocumentDto.getFieldDtoList()) {
                redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fieldDto.getId()), "read");
            }
            result.add(filterDocumentDto);
        }
        return result;
    }

    public FieldType stringToType(String type) {
        return fieldTypeRepository.findFieldTypeByTypeName(type);
    }

    public List<FieldDto> mapProjectionToDto(List<FieldProjection> fieldProjectionList) {
        List<FieldDto> fieldDtoList = addKeyInfo(fieldProjectionList);

        for (FieldProjection fieldProjection : fieldProjectionList) {
            ValueInfoData valueInfoData = new ValueInfoData();
            valueInfoData.setValue(fieldProjection.getValueName());
            valueInfoData.setValueHashId(fieldProjection.getValueHashId());

            fieldDtoList.forEach(fieldDto -> {
                if (fieldProjection.getId() == fieldDto.getId()) {
                    if (fieldProjection.getKeyType().equals("Array")) {
                        valueInfoData.setType(fieldProjection.getValueType());
                    } else if (fieldProjection.getKeyType().equals("Map")) {
                        valueInfoData.setKey(fieldProjection.getKeyName());
                        valueInfoData.setType(fieldProjection.getValueType());
                    } else {
                        valueInfoData.setType(fieldProjection.getKeyType());
                    }

                    if (fieldDto.getValueInfo() == null) {
                        fieldDto.setValueInfo(new ArrayList<>());
                    }
                    ((List<ValueInfoData>) fieldDto.getValueInfo()).add(valueInfoData);
                }
            });
        }
        return fieldDtoList;
    }

    public List<FieldDto> addKeyInfo(List<FieldProjection> fieldProjectionList) {
        Map<String, List<FieldProjection>> groupedByProductId = fieldProjectionList.stream()
                .collect(Collectors.groupingBy(fp ->
                        fp.getId() + "-" + fp.getHashId() + "-" + fp.getDocumentId() + "-" + fp.getName() + "-" + fp.getKeyType()));

        Set<String> keySet = groupedByProductId.keySet();

        ArrayList<FieldDto> fieldDtoList = new ArrayList<>();
        for (String ks : keySet) {
            FieldDto fieldDto = new FieldDto();

            String[] keyInfo = ks.split("-");
            fieldDto.setId(Long.parseLong(keyInfo[0]));
            fieldDto.setHashId(keyInfo[1]);
            fieldDto.setKey(keyInfo[3]);
            fieldDto.setType(keyInfo[4]);

            fieldDtoList.add(fieldDto);
        }
        return fieldDtoList;
    }


    @Override
    public void deleteField(String projectId, String collectionId,
                            String documentId, String fieldId, String valueId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);
        long fId = hashIdUtil.decoded(fieldId);


        if (valueId != null) {
            long vId = hashIdUtil.decoded(valueId);
            findFieldValue(id, cId, dId, fId, vId);
            fieldValueRepository.deleteById(vId);
            if (fieldValueRepository.findAllByFieldKeyId(fId).isEmpty()) {
                fieldKeyRepository.deleteById(fId);
            }
        } else {
            findFieldKey(id, cId, dId, fId);
            fieldKeyRepository.deleteById(fId);
        }
        redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "read");
        redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "write");
    }

    @Override
    public FieldDto updateField(String projectId, String collectionId, String documentId,
                                String fieldId, String valueId, ValueInfoData valueInfoData) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);
        long fId = hashIdUtil.decoded(fieldId);
        long vId = hashIdUtil.decoded(valueId);

        FieldValue existingFieldValue = findFieldValue(id, cId, dId, fId, vId);
        FieldKey existingFieldKey = fieldKeyRepository.findById(fId).orElseThrow();

        existingFieldValue.setValueName(valueInfoData.getValue());
        fieldValueRepository.save(existingFieldValue);

        redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "read");
        redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "write");

        return mapFieldKeyAndValueToFieldDto(existingFieldKey, existingFieldValue);
    }

    @Override
    public FieldDto addFieldValue(String projectId, String collectionId, String documentId,
                                  String fieldId, ValueInfoData valueInfoData) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);
        long fId = hashIdUtil.decoded(fieldId);

        FieldKey existingFieldKey = findFieldKey(id, cId, dId, fId);

        String existingKeyType = existingFieldKey.getFieldType().getTypeName();

        if (existingKeyType.equals("String") || existingKeyType.equals("Number") || existingKeyType.equals("Boolean")) {
            throw new IllegalArgumentException("This type can't add new value. ");
        }

        FieldValue fieldValue = new FieldValue();
        if (existingKeyType.equals("Map")) {
            if (valueInfoData.getKey() == null) {
                throw new IllegalArgumentException("Missing map key.");
            }
            fieldValue.setKeyName(valueInfoData.getKey());
        }
        fieldValue.setFieldKey(existingFieldKey);
        fieldValue.setValueName(valueInfoData.getValue());
        fieldValue.setFieldType(fieldTypeRepository.findFieldTypeByTypeName(valueInfoData.getType()));

        fieldValueRepository.save(fieldValue);

        redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "read");
        redisService.addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "write");

        return mapFieldKeyAndValueToFieldDto(existingFieldKey, fieldValue);
    }

    public FieldDto mapFieldKeyAndValueToFieldDto(FieldKey fieldKey, FieldValue fieldValue) {

        FieldDto fieldDto = new FieldDto();
        fieldDto.setHashId(fieldKey.getHashId());
        fieldDto.setDocumentId(fieldKey.getDocumentId());
        fieldDto.setKey(fieldKey.getName());
        fieldDto.setType(fieldKey.getFieldType().getTypeName());

        ValueInfoData valueInfoData = new ValueInfoData();
        valueInfoData.setValueHashId(fieldValue.getHashId());
        valueInfoData.setKey(fieldValue.getKeyName());
        valueInfoData.setValue(fieldValue.getValueName());
        valueInfoData.setType(fieldValue.getFieldType().getTypeName());

        fieldDto.setValueInfo(valueInfoData);
        return fieldDto;
    }

    public Document findDocumentByProjectIdAndCollectionAndId(long projectId, long collectionId, long documentId) {
        return documentRepository.findDocumentByProjectIdAndCollectionAndId(projectId, collectionId, documentId).orElseThrow();
    }

    public FieldKey findFieldKey(long projectId, long collectionId, long documentId, long fieldId) {

        int count = fieldKeyRepository.isFieldKeyExist(projectId, collectionId, documentId, fieldId);
        if (count > 0) {
            return fieldKeyRepository.findById(fieldId).orElse(null);
        } else {
            throw new NoSuchElementException();
        }
    }

    public FieldValue findFieldValue(long projectId, long collectionId,
                                     long documentId, long fieldId, long valueId) {

        int count = fieldValueRepository.isFieldValueExist(projectId, collectionId, documentId, fieldId, valueId);
        if (count > 0) {
            return fieldValueRepository.findById(valueId).orElse(null);
        } else {
            throw new NoSuchElementException();
        }
    }
}
