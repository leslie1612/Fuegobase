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

    @Autowired
    public FieldServiceImpl(CollectionRepository collectionRepository, DocumentRepository documentRepository,
                            FieldTypeRepository fieldTypeRepository, FieldKeyRepository fieldKeyRepository,
                            FieldValueRepository fieldValueRepository) {
        this.collectionRepository = collectionRepository;
        this.documentRepository = documentRepository;
        this.fieldTypeRepository = fieldTypeRepository;
        this.fieldKeyRepository = fieldKeyRepository;
        this.fieldValueRepository = fieldValueRepository;
    }

    @Override
    public void createField(String projectId, String collectionId,
                            String documentId, FieldData fieldData) {

        Document document = findDocumentByProjectIdAndCollectionAndId(projectId, collectionId, documentId);
        FieldType fieldKeyType = stringToType(fieldData.getType());
        if (fieldKeyRepository.existsByNameAndDocumentId(fieldData.getKey(), Long.parseLong(documentId))) {
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
            addReadWriteNumber(projectId, String.valueOf(savedFieldKey.getId()), "write");

        }
    }

    @Override
    public List<FieldDto> getFields(String projectId, String collectionId, String documentId) {
        Document document = findDocumentByProjectIdAndCollectionAndId(projectId, collectionId, documentId);
        List<FieldDto> fieldDtoList = mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(document.getId()));

        for (FieldDto fieldDto : fieldDtoList) {
            addReadWriteNumber(projectId, String.valueOf(fieldDto.getId()), "read");
        }
        return fieldDtoList;
    }

    @Override
    public List<FilterDocumentDto> getFieldsByFilter(String projectId, String collectionId, String filter,
                                                     String value, String type) {
        collectionRepository.findByProjectIdAndId(Long.parseLong(projectId), Long.parseLong(collectionId)).orElseThrow();
        List<Document> documents = fieldKeyRepository.getDocumentsByFilter(collectionId, filter, value, type);

        List<FilterDocumentDto> result = new ArrayList<>();
        for (Document document : documents) {
            FilterDocumentDto filterDocumentDto = new FilterDocumentDto();
            filterDocumentDto.setId(document.getId());
            filterDocumentDto.setCollectionId(document.getCollectionId());
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
            valueInfoData.setValueId(fieldProjection.getValueId());

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
                        fp.getId() + "-" + fp.getDocumentId() + "-" + fp.getName() + "-" + fp.getKeyType()));

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


    @Override
    public void deleteField(String projectId, String collectionId,
                            String documentId, String fieldId, String valueId) {

        if (valueId != null) {
            findFieldValue(projectId, collectionId, documentId, fieldId, valueId);
            fieldValueRepository.deleteById(Long.parseLong(valueId));
            if (fieldValueRepository.findAllByFieldKeyId(Long.parseLong(fieldId)).isEmpty()) {
                fieldKeyRepository.deleteById(Long.parseLong(fieldId));
            }
            log.info("Delete value by " + valueId + " successfully!");
        } else {
            findFieldKey(projectId, collectionId, documentId, fieldId);
            fieldKeyRepository.deleteById(Long.parseLong(fieldId));
            log.info("Delete field by " + fieldId + " successfully!");
        }
        addReadWriteNumber(projectId, fieldId, "read");
        addReadWriteNumber(projectId, fieldId, "write");
    }

    @Override
    public FieldDto updateField(String projectId, String collectionId, String documentId,
                                String fieldId, String valueId, ValueInfoData valueInfoData) {

        FieldValue existingFieldValue = findFieldValue(projectId, collectionId, documentId, fieldId, valueId);
        FieldKey existingFieldKey = fieldKeyRepository.findById(Long.parseLong(fieldId)).orElseThrow();

        existingFieldValue.setValueName(valueInfoData.getValue());
        fieldValueRepository.save(existingFieldValue);

        addReadWriteNumber(projectId, fieldId, "read");
        addReadWriteNumber(projectId, fieldId, "write");

        return mapFieldKeyAndValueToFieldDto(existingFieldKey, existingFieldValue);
    }

    @Override
    public FieldDto addFieldValue(String projectId, String collectionId, String documentId,
                                  String fieldId, ValueInfoData valueInfoData) {
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

        addReadWriteNumber(projectId, fieldId, "read");
        addReadWriteNumber(projectId, fieldId, "write");

        return mapFieldKeyAndValueToFieldDto(existingFieldKey, fieldValue);
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

    public FieldValue findFieldValue(String projectId, String collectionId,
                                     String documentId, String fieldId, String valueId) {
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

    public void addReadWriteNumber(String projectId, String fieldId, String action) {
        Map<String, String> readWriteLog = new HashMap<>();

        readWriteLog.put("projectId", projectId);
        readWriteLog.put("fieldId", fieldId);
        readWriteLog.put("action", action);

        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        readWriteLog.put("Timestamp", date.toString());

        logger.info(projectId + "/" + action + "/" + fieldId + "/once");
    }

}
