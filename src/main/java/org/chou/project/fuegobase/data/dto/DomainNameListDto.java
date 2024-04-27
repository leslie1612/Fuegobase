package org.chou.project.fuegobase.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.model.database.DomainNameWhitelist;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainNameListDto {
    
    @JsonProperty("apiKey")
    private String apiKey;

    @JsonProperty("domain")
    private List<DomainNameWhitelist> domainNameWhitelist;
}
