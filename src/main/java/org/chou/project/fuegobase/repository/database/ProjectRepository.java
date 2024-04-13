package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Project;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> getProjectsByUserId(Long userId);
}
