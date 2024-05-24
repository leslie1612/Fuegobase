package org.chou.project.fuegobase.model.database;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="field_type")
@Data
@NoArgsConstructor
public class FieldType {

    @Id
    private Integer id;

    @Column(name="type_name")
    private String typeName;
}
