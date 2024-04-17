package org.chou.project.fuegobase.model.dashboard;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


@Data
public class ReadWriteLog {
    private String projectId;

    private String fieldId;

    private String action;

    private Date timestamp;

}
