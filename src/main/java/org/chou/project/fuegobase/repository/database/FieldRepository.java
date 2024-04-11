package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FieldRepository extends JpaRepository<Field,Long> {
    @Query(value="SELECT d.id FROM document d JOIN collection c ON d.collection_id = c.id WHERE c.name = :collectionName AND d.name = :documentName",nativeQuery = true)
    Long documentId(@Param("collectionName") String collectionName,@Param("documentName") String documentName);
}
