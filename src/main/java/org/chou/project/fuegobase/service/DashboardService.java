package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.dashboard.LogData;
import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;

import java.util.List;

public interface DashboardService {
    double getStorage(String projectId);

    long getCollectionCount(String projectId);

    long getDocumentCount(String projectId);

    List<ReadWriteLog> getReadWriteCount(String projectId, LogData logData);


}
