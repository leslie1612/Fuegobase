package org.chou.project.fuegobase.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.data.database.ValueInfoData;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDto {
    @Id
    @JsonProperty("id")
    private long id;

    @JsonProperty("documentId")
    private long documentId;

    @JsonProperty("name")
    private String key;

    @JsonProperty("type")
    private String type;

    @JsonProperty("valueInfo")
    private Object valueInfo;
}
