package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;

import java.util.List;

public interface DashboardService {
    float getStorage(long projectID);
    long getCollectionCount(long projectId);
    long getDocumentCount(long projectId);
    List<ReadWriteLog> getLastWeekReadWriteCount(long projectId);


}
