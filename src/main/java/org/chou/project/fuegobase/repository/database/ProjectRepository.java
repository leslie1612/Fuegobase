package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
