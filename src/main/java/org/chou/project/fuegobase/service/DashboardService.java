package org.chou.project.fuegobase.service;

public interface DashboardService {
    float getStorage(long projectID);
    long getCollectionCount(long projectId);
    long getDocumentCount(long projectId);
}
