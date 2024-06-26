package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.data.dto.FilterDocumentDto;

import java.util.List;

public interface FieldService {
    void createField(String projectId, String collectionId, String documentId, FieldData fieldData);

    List<FieldDto> getFields(String projectId, String collectionId, String documentId);

    List<FilterDocumentDto> getFieldsByFilter(String projectId, String collectionId,
                                              String filter, String value, String valueType, String operator);

    FieldDto updateField(String projectId, String collectionId, String documentId,
                         String fieldId, String valueId, ValueInfoData valueInfoData);

    FieldDto addFieldValue(String projectId, String collectionId, String documentId,
                           String fieldId, ValueInfoData valueInfoData);

    void deleteField(String projectId, String collectionId, String documentId, String fieldId, String valueId);
}
