package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Boolean existsByAPIKey(String APIKey);

    Boolean existsByName(String projectName);

    List<Project> getProjectsByUserId(Long userId);

    @Query(value = "SELECT SUM(LENGTH(name)+1) / 1024 FROM project WHERE id = :id", nativeQuery = true)
    float countSizeOfProject(@Param("id") long projectId);
}
