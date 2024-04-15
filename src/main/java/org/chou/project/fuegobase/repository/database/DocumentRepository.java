package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

//    @Query(value = "SELECT id FROM collection WHERE project_id = :projectId AND name = :collectionName",
//            nativeQuery = true)
//    Long getCollectionId(@Param("projectId") long projectId, @Param("collectionName") String collectionName);

    @Query(value = "SELECT d.id FROM document d JOIN collection c ON d.collection_id = c.id WHERE c.name = :collectionName AND d.name = :documentName AND c.project_id = :projectId", nativeQuery = true)
    Long findDocumentId(@Param("projectId") long projectId, @Param("collectionName") String collectionName, @Param("documentName") String documentName);

    List<Document> findDocumentsByCollectionId(@Param("collectionId") long collectionId);

    @Query(value = "SELECT name FROM document WHERE id = :id", nativeQuery = true)
    String findNameById(@Param("id") long documentId);

    @Query(value = "SELECT d.id, d.collection_id, d.name FROM document d JOIN collection c ON d.collection_id = c.id WHERE c.project_id = :pid AND c.id = :cid AND d.id = :did", nativeQuery = true)
    Optional<Document> findDocumentByProjectIdAndCollectionAndId(@Param("pid") long projectId, @Param("cid") long collectionId, @Param("did") long documentId);
}
