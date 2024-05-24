package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.dashboard.LogData;
import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;
import org.chou.project.fuegobase.repository.dashboard.DashboardRepository;
import org.chou.project.fuegobase.repository.dashboard.ReadWriteLogRepository;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.DashboardService;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;
    private final DashboardRepository dashboardRepository;
    private final CollectionRepository collectionRepository;
    private final ReadWriteLogRepository readWriteLogRepository;
    private final HashIdUtil hashIdUtil;

    @Autowired
    public DashBoardServiceImpl(ProjectRepository projectRepository, DashboardRepository dashboardRepository,
                                CollectionRepository collectionRepository, ReadWriteLogRepository readWriteLogRepository,
                                HashIdUtil hashIdUtil) {
        this.projectRepository = projectRepository;
        this.dashboardRepository = dashboardRepository;
        this.collectionRepository = collectionRepository;
        this.readWriteLogRepository = readWriteLogRepository;
        this.hashIdUtil = hashIdUtil;
    }

    @Override
    public double getStorage(String projectId) {
        long id = hashIdUtil.decoded(projectId);
        float projectSize = projectRepository.countSizeOfProject(id);
        float collectionsSize = dashboardRepository.countCollectionsSize(id);
        float documentsSize = dashboardRepository.countDocumentsSize(id);
        float fieldKeysSize = dashboardRepository.countFieldKeysSize(id);
        float fieldValueSize = dashboardRepository.countFieldValueSize(id);

        float totalSizeInMB = (projectSize
                + collectionsSize
                + documentsSize
                + fieldKeysSize
                + fieldKeysSize
                + fieldValueSize);

        return Math.round(totalSizeInMB * 1000) * 0.001;
    }

    @Override
    public long getCollectionCount(String projectId) {
        long id = hashIdUtil.decoded(projectId);
        return collectionRepository.countAllByProjectId(id);
    }

    @Override
    public long getDocumentCount(String projectId) {
        long id = hashIdUtil.decoded(projectId);
        return dashboardRepository.countDocumentsByProjectId(id);
    }

    @Override
    public List<ReadWriteLog> getReadWriteCount(String projectId, LogData logData) {
        long id = hashIdUtil.decoded(projectId);

        ZonedDateTime startDateTime = logData.getStartDateTime();
        ZonedDateTime endDateTime = logData.getEndDateTime();

        return readWriteLogRepository.findReadWriteLogByProjectId(id, startDateTime, endDateTime);

    }

}
