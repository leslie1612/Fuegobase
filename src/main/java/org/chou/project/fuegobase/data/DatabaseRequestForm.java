package org.chou.project.fuegobase.data;

import lombok.Data;

import java.util.Map;

@Data
public class DatabaseRequestForm {

    private String APIKey;

    private String name;

    private Map<String,Object> fields;
}
