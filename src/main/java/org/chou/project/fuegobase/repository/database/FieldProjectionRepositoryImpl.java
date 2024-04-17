package org.chou.project.fuegobase.repository.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.chou.project.fuegobase.model.database.Document;
import org.springframework.data.repository.query.Param;

import java.util.List;

public class FieldProjectionRepositoryImpl implements FieldProjectionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FieldProjection> fetchAllFieldsByDocumentId(long documentId) {
        String query = """
                SELECT k.id AS id, k.document_id AS documentId, k.name, t.type_name AS keyType, v.key_name AS keyName,
                    v.id AS valueId, v.value_name AS valueName, ft.type_name AS valueType
                FROM field_key k
                JOIN field_value v ON k.id = v.field_key_id
                JOIN field_type t ON k.type_id = t.id
                JOIN field_type ft ON v.value_type_id = ft.id
                WHERE k.document_id = %d
                """;
        return entityManager.createNativeQuery(query.formatted(documentId), FieldProjection.class).getResultList();
    }

    @Override
    public List<Document> getDocumentsByFilter(String collectionId, String keyName, String valueName, String type) {
        String query = """
                SELECT d.id, d.name, d.collection_id
                FROM document d
                JOIN collection c ON d.collection_id = c.id
                JOIN field_key k ON d.id = k.document_id
                JOIN field_value v ON k.id = v.field_key_id
                JOIN field_type ft ON v.value_type_id = ft.id
                WHERE c.id = ?1 AND k.name = ?2 AND v.value_name = ?3 AND ft.type_name = ?4
                 """;

        return entityManager.createNativeQuery(query, Document.class)
                .setParameter(1, collectionId)
                .setParameter(2, keyName)
                .setParameter(3, valueName)
                .setParameter(4, type)
                .getResultList();
    }
}
