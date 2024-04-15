package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.FieldKey;
import org.chou.project.fuegobase.model.database.FieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldValueRepository extends JpaRepository<FieldValue,Long> {
    List<FieldValue> findAllByFieldKey(@Param("fieldKey")FieldKey fieldKey);
    @Query(value = """
            SELECT COUNT(*) FROM field_value v 
                JOIN field_key k ON v.field_key_id = k.id
                JOIN document d ON k.document_id = d.id 
                JOIN collection c ON d.collection_id = c.id
                JOIN project p ON c.project_id = p.id
            WHERE v.id = :vid
                AND k.id = :kid
                AND d.id = :did
                AND c.id = :cid
                AND p.id = :pid
            """, nativeQuery = true)
    int isFieldValueExist(
            @Param("pid") long projectId,
            @Param("cid") long collectionId,
            @Param("did") long documentId,
            @Param("kid") long fieldId,
            @Param("vid") long valueId
    );
}
