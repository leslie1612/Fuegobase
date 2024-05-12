package org.chou.project.fuegobase.repository.dashboard;

import org.chou.project.fuegobase.model.dashboard.ReadWriteLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface ReadWriteLogRepository extends JpaRepository<ReadWriteLog, Long> {
    
    @Query(value = """
                SELECT *
                FROM project_opertions_daily
                WHERE `operation_time` BETWEEN :startDate AND :endDate
                AND project_id = :projectId
            """, nativeQuery = true)
    List<ReadWriteLog> findReadWriteLogByProjectId(@Param("projectId") long projectId,
                                                   @Param("startDate") ZonedDateTime startDate,
                                                   @Param("endDate") ZonedDateTime endDate);

}
