package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;
import org.chou.project.fuegobase.repository.dashboard.DashboardRepository;
import org.chou.project.fuegobase.repository.dashboard.ReadWriteLogRepository;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.repository.database.DocumentRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashboardService {

    private ProjectRepository projectRepository;
    private DashboardRepository dashboardRepository;
    private CollectionRepository collectionRepository;
    private ReadWriteLogRepository readWriteLogRepository;

    @Autowired
    public DashBoardServiceImpl(ProjectRepository projectRepository,
                                DashboardRepository dashboardRepository,
                                CollectionRepository collectionRepository,
                                ReadWriteLogRepository readWriteLogRepository) {
        this.projectRepository = projectRepository;
        this.dashboardRepository = dashboardRepository;
        this.collectionRepository = collectionRepository;
        this.readWriteLogRepository = readWriteLogRepository;
    }

    @Override
    public float getStorage(long projectId) {
        float projectSize = projectRepository.countSizeOfProject(projectId);
        log.info("projectSize: " + projectSize);

        float collectionsSize = dashboardRepository.countCollectionsSize(projectId);
        log.info("collectionsSize" + collectionsSize);

        float documentsSize = dashboardRepository.countDocumentsSize(projectId);
        log.info("documentsSize" + documentsSize);

        float fieldKeysSize = dashboardRepository.countFieldKeysSize(projectId);
        log.info("fieldKeysSize" + fieldKeysSize);

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

    @Override
    public long getCollectionCount(long projectId) {
        return collectionRepository.countAllByProjectId(projectId);
    }

    @Override
    public long getDocumentCount(long projectId) {
        return dashboardRepository.countDocumentsByProjectId(projectId);
    }

    @Override
    public List<ReadWriteLog> getLastWeekReadWriteCount(long projectId) {
        return readWriteLogRepository.findLastWeekReadWriteLogByProjectId(projectId);
    }

}
