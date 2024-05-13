package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Document;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldProjectionRepository {
    List<FieldProjection> fetchAllFieldsByDocumentId(@Param("documentId") long documentId);

    List<Document> getDocumentsByFilter(String collectionId, String keyName, String valueName, String type, String operator);

    List<Document> getDocumentsByFilterWithNumber(String collectionId, String keyName, String valueName, String type, String operator);

    List<Document> getDocumentsByArrayFilter(String collectionId, String keyName, String valueName, String valueType);

    List<Document> getDocumentsByMapFilter(String collectionId, String fieldKey, String valueKey,
                                           String value, String valueType, String operator);

    List<Document> getDocumentsByMapFilterWithNumber(String collectionId, String fieldKey, String valueKey,
                                                     String value, String valueType, String operator);
}
