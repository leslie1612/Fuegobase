package org.chou.project.fuegobase.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDocumentDto {
    @Id
    @JsonProperty("id")
    @JsonIgnore
    private long id;

    @JsonProperty("hashId")
    private String hashId;

    @JsonProperty("collectionId")
    @JsonIgnore
    private long collectionId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("fields")
    private List<FieldDto> fieldDtoList;

}
