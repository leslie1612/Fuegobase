package org.chou.project.fuegobase.model.dashboard;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Entity
@Table(name = "project_opertions_daily")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadWriteLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    @JsonIgnore
    private Long projectId;

    @Column(name = "read_count")
    private int readCount;

    @Column(name = "write_count")
    private int writeCount;

    @Column(name = "operation_time")
    private ZonedDateTime date;

}
