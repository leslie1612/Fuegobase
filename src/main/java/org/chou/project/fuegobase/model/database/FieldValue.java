package org.chou.project.fuegobase.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.utils.HashIdUtil;

@Entity
@Data
@Table(name = "field_value")
@NoArgsConstructor
public class FieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @Column(name = "hash_id")
    private String hashId;

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

    @PostPersist
    private void createHashId() {
        HashIdUtil hashIdUtil = new HashIdUtil();
        this.hashId = hashIdUtil.encoded(id);
    }
}
