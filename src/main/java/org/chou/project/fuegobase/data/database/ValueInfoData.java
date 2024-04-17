package org.chou.project.fuegobase.data.database;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ValueInfoData {


    @Nullable
    private long valueId;

    @Nullable
    private String key;

    private String value;

    @Nullable
    private String type;

}
