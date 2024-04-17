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

    boolean existsByProjectIdAndName(long projectId, String name);

    Optional<Collection> findByProjectIdAndId(long projectId, long id);

    int countAllByProjectId(long projectId);
}
