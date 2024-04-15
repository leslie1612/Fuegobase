package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.model.database.Project;

import java.util.List;

public interface ProjectService {
    void createProject (ProjectData projectData);
    List<Project> getProjects(long userId);
    void deleteProject(String APIKey, String projectID);
}
