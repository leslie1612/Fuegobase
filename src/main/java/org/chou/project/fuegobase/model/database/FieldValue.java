package org.chou.project.fuegobase.model.database;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "field_value")
@NoArgsConstructor
public class FieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "field_key_id")
    private FieldKey fieldKey;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "value_name")
    private String valueName;

    @ManyToOne
    @JoinColumn(name = "value_type_id")
    private FieldType fieldType;
}
