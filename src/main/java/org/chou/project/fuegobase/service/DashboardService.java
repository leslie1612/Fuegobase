package org.chou.project.fuegobase.service;

import org.chou.project.fuegobase.data.dto.ReadWriteLogDto;

import java.text.ParseException;
import java.util.List;

public interface DashboardService {
    double getStorage(String projectId);

    long getCollectionCount(String projectId);

    long getDocumentCount(String projectId);

    List<ReadWriteLogDto> getLastWeekReadWriteCount(String projectId, String startDate, String endDate) throws ParseException;


}
