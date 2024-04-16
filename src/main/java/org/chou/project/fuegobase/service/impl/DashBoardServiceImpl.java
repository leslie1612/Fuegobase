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
    public float getStorage(long projectId) {
        float projectSize = projectRepository.countSizeOfProject(projectId);
        log.info("projectSize: " + projectSize);

        float collectionsSize = dashboardRepository.countCollectionsSize(projectId);
        log.info("collectionsSize" + collectionsSize);

        float documentsSize = dashboardRepository.countDocumentsSize(projectId);
        log.info("collectionsSize" + documentsSize);

        float fieldKeysSize = dashboardRepository.countFieldKeysSize(projectId);
        log.info("collectionsSize" + fieldKeysSize);

        float fieldValueSize = dashboardRepository.countFieldValueSize(projectId);
        log.info("fieldValueSize" + fieldValueSize);

        float totalSizeInMB = (projectSize
                + collectionsSize
                + documentsSize
                + fieldKeysSize
                + fieldKeysSize
                + fieldValueSize);

        log.info("total size in MB: " + totalSizeInMB);

        return totalSizeInMB;

    }


}
