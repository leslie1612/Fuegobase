package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.data.database.ValueInfoData;
import org.chou.project.fuegobase.data.dto.FieldDto;
import org.hibernate.mapping.ValueVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldProjectionRepository {
    List<FieldProjection> fetchAllFieldsByDocumentId(@Param("document_id") long documentId);

    List<Long> getDocumentIdsByFilter(
            @Param("name") String keyName,
            @Param("value_name") String valueName,
            @Param("type_name") String type
    );

}
