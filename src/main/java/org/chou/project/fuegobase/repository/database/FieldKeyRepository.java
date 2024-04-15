package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Document;
import org.chou.project.fuegobase.model.database.FieldKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FieldKeyRepository extends JpaRepository<FieldKey, Long>, FieldProjectionRepository {
    List<FieldKey> findAllByDocumentId(long documentId);

    @Query(value = """
            SELECT COUNT(*) FROM field_key k 
                JOIN document d ON k.document_id = d.id 
                JOIN collection c ON d.collection_id = c.id
                JOIN project p ON c.project_id = p.id
            WHERE k.id = :kid
                AND d.id = :did
                AND c.id = :cid
                AND p.id = :pid
            """, nativeQuery = true)
    int isFieldKeyExist(
            @Param("pid") long projectId,
            @Param("cid") long collectionId,
            @Param("did") long documentId,
            @Param("kid") long fieldId
    );


}
