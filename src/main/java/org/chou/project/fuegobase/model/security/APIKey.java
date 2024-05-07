package org.chou.project.fuegobase.model.security;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chou.project.fuegobase.model.database.Project;

import java.util.Date;

@Entity
@Table(name = "apikey")
@Data
@NoArgsConstructor
public class APIKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "name")
    private String name;

    @Column(name = "expiration_time")
    private Date expirationTime;

}
