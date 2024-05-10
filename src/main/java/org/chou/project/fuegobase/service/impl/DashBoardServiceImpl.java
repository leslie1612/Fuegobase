package org.chou.project.fuegobase.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.data.dto.ReadWriteLogDto;
import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;
import org.chou.project.fuegobase.repository.dashboard.DashboardRepository;
import org.chou.project.fuegobase.repository.dashboard.ReadWriteLogRepository;
import org.chou.project.fuegobase.repository.database.CollectionRepository;
import org.chou.project.fuegobase.repository.database.ProjectRepository;
import org.chou.project.fuegobase.service.DashboardService;
import org.chou.project.fuegobase.utils.HashIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashboardService {

    private ProjectRepository projectRepository;
    private DashboardRepository dashboardRepository;
    private CollectionRepository collectionRepository;
    private ReadWriteLogRepository readWriteLogRepository;
    private HashIdUtil hashIdUtil;

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

        double d = Math.round(totalSizeInMB * 1000) * 0.001;
        return d;
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
    public List<ReadWriteLogDto> getLastWeekReadWriteCount(String projectId, String startDate, String endDate) throws ParseException {
        long id = hashIdUtil.decoded(projectId);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        Date convertedStartDate = formatter.parse(startDate);
        Date convertedEndDate = formatter.parse(endDate);

        List<ReadWriteLog> readWriteLogs = readWriteLogRepository.findLastWeekReadWriteLogByProjectId(id, convertedStartDate, convertedEndDate);

        System.out.println(mapLogToDto(readWriteLogs, convertedStartDate, convertedEndDate));
        return mapLogToDto(readWriteLogs, convertedStartDate, convertedEndDate);
    }

    public List<ReadWriteLogDto> mapLogToDto(List<ReadWriteLog> readWriteLogList, Date startDate, Date endDate) {
        List<Date> allDates = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        while (startDate.getTime() <= endDate.getTime()) {
            allDates.add(startDate);
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }

        List<ReadWriteLogDto> readWriteLogDtoList = new ArrayList<>();

        for (Date date : allDates) {
            ReadWriteLogDto readWriteLogDto = new ReadWriteLogDto();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = formatter.format(date);
            readWriteLogDto.setDate(formattedDate);

            for (ReadWriteLog readWriteLog : readWriteLogList) {
                Date logDate = Date.from(readWriteLog.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (date.equals(logDate)) {
                    readWriteLogDto.setReadCount(readWriteLog.getReadCount());
                    readWriteLogDto.setWriteCount(readWriteLog.getWriteCount());
                    break;
                } else {
                    readWriteLogDto.setReadCount(0);
                    readWriteLogDto.setReadCount(0);
                }
            }

            readWriteLogDtoList.add(readWriteLogDto);
        }

        return readWriteLogDtoList;

    }

}
