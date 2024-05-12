package org.chou.project.fuegobase.data.dashboard;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class LogData {
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
}
