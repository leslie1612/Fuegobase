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
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FieldServiceImpl implements FieldService {
    private static final Logger logger = LoggerFactory.getLogger(FieldServiceImpl.class);
    private CollectionRepository collectionRepository;
    private DocumentRepository documentRepository;
    private FieldTypeRepository fieldTypeRepository;
    private FieldKeyRepository fieldKeyRepository;
    private FieldValueRepository fieldValueRepository;
    private HashIdUtil hashIdUtil;

    @Autowired
    public FieldServiceImpl(CollectionRepository collectionRepository, DocumentRepository documentRepository,
                            FieldTypeRepository fieldTypeRepository, FieldKeyRepository fieldKeyRepository,
                            FieldValueRepository fieldValueRepository, HashIdUtil hashIdUtil) {
        this.collectionRepository = collectionRepository;
        this.documentRepository = documentRepository;
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldKeyRepository = fieldKeyRepository;
        this.fieldValueRepository = fieldValueRepository;
        this.hashIdUtil = hashIdUtil;
    }

    @Override
    public void createField(String projectId, String collectionId,
                            String documentId, FieldData fieldData) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);

        Document document = findDocumentByProjectIdAndCollectionAndId(projectId, collectionId, documentId);
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
            addReadWriteNumber(String.valueOf(dId), String.valueOf(savedFieldKey.getId()), "write");

        }
    }

    @Override
    public List<FieldDto> getFields(String projectId, String collectionId, String documentId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);

        Document document = findDocumentByProjectIdAndCollectionAndId(projectId, collectionId, documentId);
        List<FieldDto> fieldDtoList = mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(document.getId()));

        for (FieldDto fieldDto : fieldDtoList) {
            addReadWriteNumber(String.valueOf(id), String.valueOf(fieldDto.getId()), "read");
        }
        return fieldDtoList;
    }

    @Override
    public List<FilterDocumentDto> getFieldsByFilter(String projectId, String collectionId, String filter,
                                                     String value, String valueType, String operator) throws IllegalArgumentException {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);

        collectionRepository.findByProjectIdAndId(id, cId).orElseThrow();
        String[] keys = filter.split("\\.");
        List<Document> documents = new ArrayList<>();

        if (operator.equals("CONTAINS") && keys.length != 1) {
            System.out.println("is error");
            throw new IllegalArgumentException();
        }

        if (operator.equals("CONTAINS") && keys.length <= 1) {
            System.out.println("is array");
            documents = fieldKeyRepository.getDocumentsByArrayFilter(String.valueOf(cId), keys[0], value, valueType);
        } else {
            if (keys.length > 1 && !operator.equals("CONTAINS")) {
                System.out.println("is map");
                String fieldKey = keys[0];
                String valueKey = keys[1];
                documents = fieldKeyRepository.getDocumentsByMapFilter(String.valueOf(cId), fieldKey, valueKey,
                        value, valueType, operator);
            } else if (keys.length <= 1) {
                System.out.println("others");
                documents = fieldKeyRepository.getDocumentsByFilter(String.valueOf(cId), keys[0], value, valueType, operator);
            }
        }

        List<FilterDocumentDto> result = new ArrayList<>();
        for (Document document : documents) {
            FilterDocumentDto filterDocumentDto = new FilterDocumentDto();
//            filterDocumentDto.setId(document.getId());
            filterDocumentDto.setHashId(document.getHashId());
//            filterDocumentDto.setCollectionId(document.getCollectionId());
            filterDocumentDto.setName(document.getName());
            filterDocumentDto.setFieldDtoList(mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(document.getId())));

            for (FieldDto fieldDto : filterDocumentDto.getFieldDtoList()) {
                addReadWriteNumber(projectId, String.valueOf(fieldDto.getId()), "read");
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
//            valueInfoData.setValueId(fieldProjection.getValueId());
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
//            fieldDto.setDocumentId(Long.parseLong(keyInfo[2]));
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
            findFieldValue(projectId, collectionId, documentId, fieldId, valueId);
            fieldValueRepository.deleteById(vId);
            if (fieldValueRepository.findAllByFieldKeyId(fId).isEmpty()) {
                fieldKeyRepository.deleteById(fId);
            }
        } else {
            findFieldKey(projectId, collectionId, documentId, fieldId);
            fieldKeyRepository.deleteById(fId);
        }
        addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "read");
        addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "write");
    }

    @Override
    public FieldDto updateField(String projectId, String collectionId, String documentId,
                                String fieldId, String valueId, ValueInfoData valueInfoData) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);
        long fId = hashIdUtil.decoded(fieldId);
        long vId = hashIdUtil.decoded(valueId);

        FieldValue existingFieldValue = findFieldValue(projectId, collectionId, documentId, fieldId, valueId);
        FieldKey existingFieldKey = fieldKeyRepository.findById(fId).orElseThrow();

        existingFieldValue.setValueName(valueInfoData.getValue());
        fieldValueRepository.save(existingFieldValue);

        addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "read");
        addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "write");

        return mapFieldKeyAndValueToFieldDto(existingFieldKey, existingFieldValue);
    }

    @Override
    public FieldDto addFieldValue(String projectId, String collectionId, String documentId,
                                  String fieldId, ValueInfoData valueInfoData) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);
        long fId = hashIdUtil.decoded(fieldId);

        FieldKey existingFieldKey = findFieldKey(projectId, collectionId, documentId, fieldId);

        String existingKeyType = existingFieldKey.getFieldType().getTypeName();

        if (existingKeyType.equals("String") || existingKeyType.equals("Number") || existingKeyType.equals("Boolean")) {
            throw new IllegalArgumentException();
        }

        FieldValue fieldValue = new FieldValue();
        if (existingKeyType.equals("Map")) {
            if (valueInfoData.getKey() == null) {
                throw new IllegalArgumentException();
            }
            fieldValue.setKeyName(valueInfoData.getKey());
        }
        fieldValue.setFieldKey(existingFieldKey);
        fieldValue.setValueName(valueInfoData.getValue());
        fieldValue.setFieldType(fieldTypeRepository.findFieldTypeByTypeName(valueInfoData.getType()));

        fieldValueRepository.save(fieldValue);

        addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "read");
        addReadWriteNumber(String.valueOf(id), String.valueOf(fId), "write");

        return mapFieldKeyAndValueToFieldDto(existingFieldKey, fieldValue);
    }

    public FieldDto mapFieldKeyAndValueToFieldDto(FieldKey fieldKey, FieldValue fieldValue) {

        try {
            FieldDto fieldDto = new FieldDto();
//            fieldDto.setId(fieldKey.getId());
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
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public Document findDocumentByProjectIdAndCollectionAndId(String projectId, String collectionId, String documentId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);

        return documentRepository.findDocumentByProjectIdAndCollectionAndId(id, cId, dId).orElseThrow();

    }

    public FieldKey findFieldKey(String projectId, String collectionId, String documentId, String fieldId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);
        long fId = hashIdUtil.decoded(fieldId);

        int count = fieldKeyRepository.isFieldKeyExist(id, cId, dId, fId);
        if (count > 0) {
            return fieldKeyRepository.findById(fId).orElse(null);
        } else {
            throw new NoSuchElementException();
        }
    }

    public FieldValue findFieldValue(String projectId, String collectionId,
                                     String documentId, String fieldId, String valueId) {
        long id = hashIdUtil.decoded(projectId);
        long cId = hashIdUtil.decoded(collectionId);
        long dId = hashIdUtil.decoded(documentId);
        long fId = hashIdUtil.decoded(fieldId);
        long vId = hashIdUtil.decoded(valueId);

        int count = fieldValueRepository.isFieldValueExist(id, cId, dId, fId, vId);
        if (count > 0) {
            return fieldValueRepository.findById(vId).orElse(null);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void addReadWriteNumber(String projectId, String fieldId, String action) {
        Map<String, String> readWriteLog = new HashMap<>();

        readWriteLog.put("projectId", projectId);
        readWriteLog.put("fieldId", fieldId);
        readWriteLog.put("action", action);

        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        readWriteLog.put("Timestamp", date.toString());

        logger.info("/" + projectId + "/" + action + "/" + fieldId + "/once");

    }
}
