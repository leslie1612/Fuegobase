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

    @Column(name="field_type_id")
    private int fieldTypeId;

    @Column(name="field_key_name")
    private String fieldKey;

    @Column(name="field_value")
    private String fieldValue;
}
