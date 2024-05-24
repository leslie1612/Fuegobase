package org.chou.project.fuegobase.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.utils.HashIdUtil;

@Entity
@Data
@Table(name = "field_key")
@NoArgsConstructor
public class FieldKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(name = "hash_id")
    private String hashId;

    @Column(name = "document_id")
    @JsonIgnore
    private long documentId;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private FieldType fieldType;

    @Column(name = "name")
    private String name;

    @PostPersist
    private void createHashId() {
        HashIdUtil hashIdUtil = new HashIdUtil();
        this.hashId = hashIdUtil.encoded(id);
    }
}
