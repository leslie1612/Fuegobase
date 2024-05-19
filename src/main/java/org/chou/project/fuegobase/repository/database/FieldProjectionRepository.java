package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Document;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldProjectionRepository {
    List<FieldProjection> fetchAllFieldsByDocumentId(@Param("documentId") long documentId);

    List<Document> getDocumentsByFilter(long collectionId, String keyName, String valueName, String type, String operator);

    List<Document> getDocumentsByFilterWithNumber(long collectionId, String keyName, String valueName, String type, String operator);

    List<Document> getDocumentsByArrayFilter(long collectionId, String keyName, String valueName, String valueType);

    List<Document> getDocumentsByMapFilter(long collectionId, String fieldKey, String valueKey,
                                           String value, String valueType, String operator);

    List<Document> getDocumentsByMapFilterWithNumber(long collectionId, String fieldKey, String valueKey,
                                                     String value, String valueType, String operator);
}
