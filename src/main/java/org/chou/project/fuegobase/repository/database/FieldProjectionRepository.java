package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.chou.project.fuegobase.model.database.Document;
import org.hibernate.mapping.ValueVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldProjectionRepository {
    List<FieldProjection> fetchAllFieldsByDocumentId(@Param("documentId") long documentId);

    List<Long> getDocumentIdsByFilter(
            @Param("collectionId") String collectionId,
            @Param("name") String keyName,
            @Param("valueName") String valueName,
            @Param("typeName") String type
    );

    List<Document> getDocumentsByFilter(
            @Param("collectionId") String collectionId,
            @Param("name") String keyName,
            @Param("valueName") String valueName,
            @Param("typeName") String type
    );

}
