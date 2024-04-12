package org.chou.project.fuegobase.model.database;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="field")
@Data
@NoArgsConstructor
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="document_id")
    private Long documentId;

    @Column(name="key_type_id")
    private int keyTypeId;

    @Column(name="key_name")
    private String keyName;

    @Column(name="value_type_id")
    private int valueTypeId;

    @Column(name="value_name")
    private String valueName;
}
