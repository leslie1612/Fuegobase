package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;

import java.util.List;

public interface DashboardService {
    float getStorage(String projectId);

    long getCollectionCount(String projectId);

    long getDocumentCount(String projectId);

    List<ReadWriteLog> getLastWeekReadWriteCount(String projectId);


}
