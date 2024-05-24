package org.chou.project.fuegobase.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse<T> {

    @JsonProperty("data")
    T data;

    public GenericResponse(T data){
        this.data = data;
    }
}
