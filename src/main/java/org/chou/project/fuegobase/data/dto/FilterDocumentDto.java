package org.chou.project.fuegobase.data.dto;

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
    private long id;

    @JsonProperty("collectionId")
    private long collectionId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("fields")
    private List<FieldDto> fieldDtoList;

}
