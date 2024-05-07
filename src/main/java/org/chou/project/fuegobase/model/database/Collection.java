package org.chou.project.fuegobase.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.utils.HashIdUtil;

@Entity
@Table(name = "collection")
@Data
@NoArgsConstructor
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "hash_id")
    private String hashId;

    @JsonIgnore
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "name")
    private String name;

    @PostPersist
    private void createHashId() {
        HashIdUtil hashIdUtil = new HashIdUtil();
        this.hashId = hashIdUtil.encoded(id);
    }

}
