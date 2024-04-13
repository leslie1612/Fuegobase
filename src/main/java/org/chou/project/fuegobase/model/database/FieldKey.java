package org.chou.project.fuegobase.model.database;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "field_key")
@NoArgsConstructor
public class FieldKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

//    @ManyToOne()
//    @JoinColumn(name = "document_id")
//    private Document document;

    @Column(name = "document_id")
    private long documentId;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private FieldType fieldType;

    @Column(name = "name")
    private String name;
}
