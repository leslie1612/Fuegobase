package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection,Long> {
}
