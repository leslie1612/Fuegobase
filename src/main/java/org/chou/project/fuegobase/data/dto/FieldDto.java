package org.chou.project.fuegobase.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDto {
    @Id
    @JsonProperty("id")
    @JsonIgnore
    private long id;

    @JsonProperty("id")
    private String hashId;

    @JsonProperty("documentId")
    @JsonIgnore
    private long documentId;

    @JsonProperty("name")
    private String key;

    @JsonProperty("type")
    private String type;

    @JsonProperty("valueInfo")
    private Object valueInfo;
}
