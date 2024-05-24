package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FieldTypeRepository extends JpaRepository<FieldType, Long> {

    FieldType findFieldTypeByTypeName(@Param("type_name") String typeName);
}
