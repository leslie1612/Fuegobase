package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Boolean existsByNameAndCollectionId(String DocumentName, long collectionId);

    List<Document> findDocumentsByCollectionId(@Param("collectionId") long collectionId);

    @Query(value = "SELECT d.id, d.collection_id, d.name, d.hash_id FROM document d JOIN collection c ON d.collection_id = c.id WHERE c.project_id = :pid AND c.id = :cid AND d.id = :did", nativeQuery = true)
    Optional<Document> findDocumentByProjectIdAndCollectionAndId(@Param("pid") long projectId, @Param("cid") long collectionId, @Param("did") long documentId);
}
