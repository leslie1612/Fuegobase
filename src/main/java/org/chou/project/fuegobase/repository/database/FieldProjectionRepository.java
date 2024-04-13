package org.chou.project.fuegobase.repository.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldProjectionRepository {
    List<FieldProjection> fetchAllFieldsByDocumentId(@Param("document_id") long documentId);
}
