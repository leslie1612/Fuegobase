package org.chou.project.fuegobase.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInDto {
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("accessExpired")
    private Long accessExpired;

    @JsonProperty("user")
    private UserDto userDto;

}
