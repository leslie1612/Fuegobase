package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FieldRepository extends JpaRepository<Field, Long> {

    // 確認 collection_id
    @Query(value = "SELECT d.id FROM document d JOIN collection c ON d.collection_id = c.id WHERE c.name = :collectionName AND d.name = :documentName AND c.project_id = :projectId", nativeQuery = true)
    Long getDocumentId(@Param("projectId") long projectId, @Param("collectionName") String collectionName, @Param("documentName") String documentName);
}
