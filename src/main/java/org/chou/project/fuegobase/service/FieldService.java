package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.database.FieldKeyData;
import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.data.dto.FilterDocumentDto;

import java.util.List;

public interface FieldService {
    void createField(String APIKey, String projectId, String collectionId,
                     String documentId, FieldData fieldData);

//    List<Field> getFields(String APIKey, String projectId, String collectionName, String documentName);
    List<FieldDto> getFields(String APIKey, String projectId, String collectionId, String documentId);

    List<FilterDocumentDto> getFieldsByFilter(String APIKey, String projectId,
                                              String collectionId, String filter,
                                              String value, String type);

    FieldDto renameField(String APIKey, String projectId, String collectionId,
                     String documentId, String FieldId, FieldKeyData updatedFieldData);

    FieldDto postField(String APIKey, String projectId, String collectionId,
                         String documentId, String FieldId, ValueInfoData valueInfoData);
}
