package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.FieldKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldKeyRepository extends JpaRepository<FieldKey, Long>, FieldProjectionRepository {
    List<FieldKey> findAllByDocumentId(long documentId);

}
