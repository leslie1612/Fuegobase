package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.data.dto.DomainNameListDto;
import org.chou.project.fuegobase.exception.ResourceNotFoundException;
import org.chou.project.fuegobase.model.database.Project;

import java.util.List;

public interface ProjectService {

    void createProject(ProjectData projectData, String token);

    List<Project> getProjects(String token);

    void deleteProject(String projectId, String token) throws ResourceNotFoundException;

    void addDomainNameWhiteList(String projectId, DomainNameData domainNameData);

    DomainNameListDto getDomainWhiteList(String projectId);

    void deleteDomainName(String projectId, long domainNameId);

}
