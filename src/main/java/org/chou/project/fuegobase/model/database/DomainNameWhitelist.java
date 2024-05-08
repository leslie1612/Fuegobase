package org.chou.project.fuegobase.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "domain_name_whitelist")
@Data
@NoArgsConstructor
public class DomainNameWhitelist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "project_id")
    @JsonIgnore
    private long projectId;

    @Column(name = "name")
    private String domainName;

    @Column(name = "type")
    private String type;
}
