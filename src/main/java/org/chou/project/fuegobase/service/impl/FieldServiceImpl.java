package org.chou.project.fuegobase.service.impl;

import jakarta.persistence.Id;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
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
                            String collectionName, String documentName, FieldData fieldData) {
        // TODO 驗證 APIKEY

        try {
            long documentId = getDocumentId(projectId, collectionName, documentName);
            FieldType fieldKeyType = getType(fieldData.getType());

            FieldKey fieldKey = new FieldKey();
            fieldKey.setDocumentId(documentId);
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
    public List<FieldDto> getFields(String APIKey, String projectId, String collectionName, String documentName) {
        long documentId = getDocumentId(projectId, collectionName, documentName);
        return mapProjectionToDto(fieldKeyRepository.fetchAllFieldsByDocumentId(documentId));

    }

    public long getDocumentId(String projectId, String collectionName, String documentName) {
        return documentRepository.findDocumentId(Long.parseLong(projectId), collectionName, documentName);
    }

    public FieldType getType(String type) {
        return fieldTypeRepository.findFieldTypeByTypeName(type);
    }

    public List<FieldDto> mapProjectionToDto(List<FieldProjection> fieldProjectionList) {

        List<FieldDto> fieldDtoList = addKeyInfo(fieldProjectionList);

        for (FieldProjection fieldProjection : fieldProjectionList) {

            fieldDtoList.forEach(fieldDto -> {

                if (fieldProjection.getId() == fieldDto.getId()) {

                    switch (fieldProjection.getKeyType()) {

                        case "String":
                            fieldDto.setValue(fieldProjection.getValueName());
                            break;

                        case "Number":
                            fieldDto.setValue(Integer.parseInt(fieldProjection.getValueName()));
                            break;

                        case "Boolean":
                            fieldDto.setValue(Boolean.parseBoolean(fieldProjection.getValueName()));
                            break;

                        case "Array":
                            Object arrayValue = convertType(fieldProjection.getValueType(), fieldProjection.getValueName());
                            if (fieldDto.getValue() == null) {
                                fieldDto.setValue(new ArrayList<>());
                            }
                            ((List<Object>) fieldDto.getValue()).add(arrayValue);
                            break;

                        case "Map":
                            Object mapValue = convertType(fieldProjection.getValueType(), fieldProjection.getValueName());
                            if (fieldDto.getValue() == null) {
                                fieldDto.setValue(new HashMap<>());
                            }
                            ((Map<String, Object>) fieldDto.getValue()).put(fieldProjection.getKeyName(), mapValue);
//                            fieldDto.setValue(hashMap);
                            break;
                    }
                }
            });
        }

        return fieldDtoList;

    }

    public List<FieldDto> addKeyInfo(List<FieldProjection> fieldProjectionList) {

        Map<String, List<FieldProjection>> groupedByProductId = fieldProjectionList.stream()
                .collect(Collectors.groupingBy(fp -> fp.getId() + "-" + fp.getName()));

        Set<String> idAndNameSet = groupedByProductId.keySet();

        ArrayList<FieldDto> fieldDtoList = new ArrayList<>();
        for (String idAndName : idAndNameSet) {
            FieldDto fieldDto = new FieldDto();

            String[] title = idAndName.split("-");
            fieldDto.setId(Long.parseLong(title[0]));
            fieldDto.setKeyName(title[1]);

            fieldDtoList.add(fieldDto);
        }
        return fieldDtoList;
    }

    public Object convertType(String type, String name) {
        return switch (type) {
            case "String" -> name;
            case "Number" -> Long.valueOf(name);
            case "Boolean" -> Boolean.valueOf(name);
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }

}
