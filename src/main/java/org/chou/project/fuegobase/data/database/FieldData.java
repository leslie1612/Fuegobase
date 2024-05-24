package org.chou.project.fuegobase.data.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class FieldData {

    private String type;

    private String key;

    private List<ValueInfoData> valueInfo;

}
