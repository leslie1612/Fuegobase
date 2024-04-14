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
    @JsonProperty("documentId")
    private long id;

    @JsonProperty("documentName")
    private String name;

    @JsonProperty("field")
    private List<FieldDto> fieldDtoList;

}
