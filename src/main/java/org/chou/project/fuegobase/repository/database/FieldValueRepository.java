package org.chou.project.fuegobase.repository.database;

import org.chou.project.fuegobase.model.database.FieldKey;
import org.chou.project.fuegobase.model.database.FieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FieldValueRepository extends JpaRepository<FieldValue,Long> {
    List<FieldValue> findAllByFieldKey(@Param("fieldKey")FieldKey fieldKey);
}
