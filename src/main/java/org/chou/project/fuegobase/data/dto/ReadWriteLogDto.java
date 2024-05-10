package org.chou.project.fuegobase.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadWriteLogDto {

    @JsonProperty("readCount")
    private int readCount;

    @JsonProperty("writeCount")
    private int writeCount;

    @JsonProperty("date")
    private String date;
}
