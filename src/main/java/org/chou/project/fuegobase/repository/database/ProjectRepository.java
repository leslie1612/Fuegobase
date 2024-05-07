package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "SELECT COUNT(*) FROM project p JOIN apikey k ON k.project_id = p.id WHERE p.id = :id AND k.name = :name", nativeQuery = true)
    Integer validateByIdAndApiKey(@Param("id") long projectId, @Param("name") String apikey);

    Boolean existsByNameAndUserId(String projectName, long userId);

    List<Project> getProjectsByUserId(Long userId);

    @Query(value = "SELECT SUM(LENGTH(name)+1) / 1024 FROM project WHERE id = :id", nativeQuery = true)
    float countSizeOfProject(@Param("id") long projectId);

    Optional<Project> findByHashId(String hashId);
}
