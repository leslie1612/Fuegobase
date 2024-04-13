package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.FieldData;
import org.chou.project.fuegobase.data.dto.FieldDto;

import java.util.List;

public interface FieldService {
    void createField(String APIKey, String projectId, String collectionName,
                     String documentName, FieldData fieldData);

//    List<Field> getFields(String APIKey, String projectId, String collectionName, String documentName);
    List<FieldDto> getFields(String APIKey, String projectId, String collectionName, String documentName);
}
