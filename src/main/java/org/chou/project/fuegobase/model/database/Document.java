package org.chou.project.fuegobase.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.utils.HashIdUtil;

@Entity
@Table(name = "document")
@Data
@NoArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "hash_id")
    private String hashId;

    @JsonIgnore
    @Column(name = "collection_id")
    private long collectionId;

    @Column(name = "name")
    private String name;

    @PostPersist
    private void createHashId() {
        HashIdUtil hashIdUtil = new HashIdUtil();
        this.hashId = hashIdUtil.encoded(id);
    }

}
