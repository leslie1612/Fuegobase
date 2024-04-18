package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.DomainNameWhitelist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainNameRepository extends JpaRepository<DomainNameWhitelist,Long> {
    Boolean existsByProjectIdAndDomainName(long projectId, String domainName);
}
