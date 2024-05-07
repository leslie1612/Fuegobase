package org.chou.project.fuegobase.repository.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.chou.project.fuegobase.model.database.Document;

import java.util.List;

@Slf4j
public class FieldProjectionRepositoryImpl implements FieldProjectionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FieldProjection> fetchAllFieldsByDocumentId(long documentId) {
        String query = """
                SELECT k.id AS id, k.hash_id AS hashId, k.document_id AS documentId,k.name, t.type_name AS keyType, v.key_name AS keyName,
                    v.id AS valueId, v.value_name AS valueName, ft.type_name AS valueType
                FROM field_key k
                JOIN field_value v ON k.id = v.field_key_id
                JOIN field_type t ON k.type_id = t.id
                JOIN field_type ft ON v.value_type_id = ft.id
                WHERE k.document_id = %d
                """;
//        String query = """
//                SELECT k.id AS id, k.document_id AS documentId, k.name, t.type_name AS keyType, v.key_name AS keyName,
//                    v.id AS valueId, v.value_name AS valueName, ft.type_name AS valueType
//                FROM field_key k
//                JOIN field_value v ON k.id = v.field_key_id
//                JOIN field_type t ON k.type_id = t.id
//                JOIN field_type ft ON v.value_type_id = ft.id
//                WHERE k.document_id = %d
//                """;
        return entityManager.createNativeQuery(query.formatted(documentId), FieldProjection.class).getResultList();
    }

    @Override
    public List<Document> getDocumentsByFilter(String collectionId, String keyName, String valueName,
                                               String type, String operator) {
        String op = getOperator(operator);

        String query = """
                SELECT d.id, d.name, d.collection_id
                FROM document d
                JOIN collection c ON d.collection_id = c.id
                JOIN field_key k ON d.id = k.document_id
                JOIN field_value v ON k.id = v.field_key_id
                JOIN field_type ft ON k.type_id = ft.id
                WHERE c.id = ?1 AND k.name = ?2 AND ft.type_name = ?3 AND v.value_name """ + op + "?4 AND v.key_name IS NULL";

        return entityManager.createNativeQuery(query, Document.class)
                .setParameter(1, collectionId)
                .setParameter(2, keyName)
                .setParameter(3, type)
                .setParameter(4, valueName)
                .getResultList();
    }

    @Override
    public List<Document> getDocumentsByArrayFilter(String collectionId, String keyName, String valueName, String valueType) {

        String query = """
                SELECT d.id, d.name, d.collection_id
                FROM document d
                JOIN collection c ON d.collection_id = c.id
                JOIN field_key k ON d.id = k.document_id
                JOIN field_value v ON k.id = v.field_key_id
                JOIN field_type ft ON v.value_type_id = ft.id
                WHERE c.id = ?1 AND k.name = ?2 AND v.value_name = ?3 AND ft.type_name = ?4 AND k.type_id = 4;
                """;

        return entityManager.createNativeQuery(query, Document.class)
                .setParameter(1, collectionId)
                .setParameter(2, keyName)
                .setParameter(3, valueName)
                .setParameter(4, valueType)
                .getResultList();
    }

    @Override
    public List<Document> getDocumentsByMapFilter(String collectionId, String fieldKey, String valueKey,
                                                  String valueName, String valueType, String operator) {
        String op = getOperator(operator);

        String query = """
                SELECT d.id, d.name, d.collection_id
                FROM document d
                JOIN collection c ON d.collection_id = c.id
                JOIN field_key k ON d.id = k.document_id
                JOIN field_value v ON k.id = v.field_key_id
                JOIN field_type ft ON v.value_type_id = ft.id
                WHERE c.id = ?1 AND k.name = ?2 AND  v.key_name = ?3 AND v.value_name""" + op + " ?4 AND ft.type_name = ?5 AND k.type_id = 5";
        ;

        return entityManager.createNativeQuery(query, Document.class)
                .setParameter(1, collectionId)
                .setParameter(2, fieldKey)
                .setParameter(3, valueKey)
                .setParameter(4, valueName)
                .setParameter(5, valueType)
                .getResultList();
    }

    public String getOperator(String operator) {
        enum Operator {
            EQUAL,
            GREATER_THAN,
            LESS_THAN,
            GREATER_THAN_OR_EQUAL,
            LESS_THAN_OR_EQUAL,
            CONTAINS
        }
        String op = "";

        switch (Operator.valueOf(operator)) {
            case EQUAL:
                op = "=";
                break;
            case GREATER_THAN:
                op = ">";
                break;
            case LESS_THAN:
                op = "<";
                break;
            case GREATER_THAN_OR_EQUAL:
                op = ">=";
                break;
            case LESS_THAN_OR_EQUAL:
                op = "<=";
                break;
        }
        return op;
    }


}
