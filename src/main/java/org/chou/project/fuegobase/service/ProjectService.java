package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.database.DomainNameData;
import org.chou.project.fuegobase.data.database.ProjectData;
import org.chou.project.fuegobase.data.dto.DomainNameListDto;
import org.chou.project.fuegobase.exception.APIKeyException;
import org.chou.project.fuegobase.model.database.Project;

import java.util.List;

public interface ProjectService {

    void createProject(ProjectData projectData, String token) throws APIKeyException;

    List<Project> getProjects(String token);

    void deleteProject(String projectId);

    void addDomainNameWhiteList(long projectId, DomainNameData domainNameData);

    DomainNameListDto getDomainWhiteList(long projectId);

    void deleteDomainName(long projectId, long domainNameId);

}
