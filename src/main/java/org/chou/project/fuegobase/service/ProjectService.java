package org.chou.project.fuegobase.service;

import jakarta.servlet.http.HttpServletRequest;
import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.model.database.Project;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ProjectService {

    void createProject(ProjectData projectData);

    List<Project> getProjects(long userId);

    void deleteProject(String projectId);

    void addDomainNameWhiteList(long projectId, DomainNameData domainNameData);

}
