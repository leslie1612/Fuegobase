package org.chou.project.fuegobase.repository.dashboard;

import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReadWriteLogRepository extends JpaRepository<ReadWriteLog, Long> {

    ReadWriteLog findReadWriteLogByProjectIdAndDate(long projectId, LocalDate date);
    
    @Query(value = """
                SELECT *
                FROM project_opertions_daily
                WHERE `date` BETWEEN :startDate AND :endDate
                AND project_id = :projectId
            """, nativeQuery = true)
    List<ReadWriteLog> findLastWeekReadWriteLogByProjectId(@Param("projectId") long projectId, @Param("startDate") Date startDate,
                                                           @Param("endDate") Date endDate);

    Optional<ReadWriteLog> findByDateAndProjectId(LocalDate date, long projectId);

}
