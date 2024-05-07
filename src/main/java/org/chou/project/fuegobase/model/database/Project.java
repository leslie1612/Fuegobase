package org.chou.project.fuegobase.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.utils.HashIdUtil;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
public class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "hash_id")
    private String hashId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    //    @PostLoad
//    public void postLoad() {
//        HashIdUtil hashIdUtil = new HashIdUtil();
//        this.hashId = hashIdUtil.encoded(id);
//    }
    @PostPersist
    private void createHashId() {
        HashIdUtil hashIdUtil = new HashIdUtil();
        this.hashId = hashIdUtil.encoded(id);
    }

}
