package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.repository.dashboard.DashboardRepository;
import org.chou.project.fuegobase.repository.database.DocumentRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashboardService {

    private ProjectRepository projectRepository;
    private DashboardRepository dashboardRepository;

    @Autowired
    public DashBoardServiceImpl(ProjectRepository projectRepository, DashboardRepository dashboardRepository) {
        this.projectRepository = projectRepository;
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public float getStorage(String projectId) {
        float projectLength = projectRepository.countLengthOfProject(projectId);
        System.out.println(projectLength);
        float collectionsLength = dashboardRepository.countCollectionsLength(projectId);
        System.out.println(collectionsLength);
        float documentsLength = dashboardRepository.countDocumentsLength(projectId);
        System.out.println(documentsLength);
        float fieldKeysLength = dashboardRepository.countFieldKeysLength(projectId);
        System.out.println(fieldKeysLength);
        float fieldValueLength = dashboardRepository.countFieldValueLength(projectId);
        System.out.println(fieldValueLength);

        float totalSizeInMb = (projectLength
                + collectionsLength
                + documentsLength
                + fieldKeysLength
                + fieldKeysLength
                + fieldValueLength);

        log.info("total size in mb: " + totalSizeInMb);

        return totalSizeInMb;

    }


}
