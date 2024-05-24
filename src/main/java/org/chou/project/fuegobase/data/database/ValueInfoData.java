package org.chou.project.fuegobase.data.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class ValueInfoData {


    @Nullable
    @JsonIgnore
    private long valueId;

    @Nullable
    private String valueHashId;

    @Nullable
    private String key;

    private String value;

    @Nullable
    private String type;

}
