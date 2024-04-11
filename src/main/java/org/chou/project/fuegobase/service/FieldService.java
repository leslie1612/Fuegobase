package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.FieldData;

public interface FieldService {
    void createField (String APIKey, String projectId, String collectionName,
                      String documentName, FieldData fieldData);
}
