package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> getCollectionsByProjectId(long projectId);

    boolean existsByProjectIdAndName(long projectId, String name);

    Optional<Collection> findByProjectIdAndId(long projectId, long id);

    int countAllByProjectId(long projectId);
}
