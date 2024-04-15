package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Collection;
import org.chou.project.fuegobase.model.database.Document;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> getCollectionsByProjectId(Long projectId);

    @Query(value = "SELECT id FROM collection WHERE project_id = :projectId AND name = :collectionName",
            nativeQuery = true)
    Long findCollectionId(@Param("projectId") long projectId, @Param("collectionName") String collectionName);

//    @Query(value = "SELECT * FROM collection WHERE project_id = :projectId AND name = :collectionName",
//            nativeQuery = true)
//    Collection findCollection(@Param("projectId") long projectId, @Param("collectionName") String collectionName);

    //    @Query(value = "SELECT COUNT(*) FROM collection WHERE project_id = :pid AND id = :cid",nativeQuery = true)
    Optional<Collection> findByProjectIdAndId(long projectId, long id);
}
