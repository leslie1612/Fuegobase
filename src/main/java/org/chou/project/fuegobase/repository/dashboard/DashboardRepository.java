package org.chou.project.fuegobase.repository.dashboard;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public float countCollectionsSize(long projectId) {
        String query = "SELECT SUM(LENGTH(name) + 1) / 1024 FROM collection WHERE project_id = ?1";
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, projectId);
        Object result = nativeQuery.getSingleResult();
        return result != null ? ((Number) result).floatValue() : 0.0f;
    }

    public float countDocumentsSize(long projectId) {
        String query = """
                SELECT SUM(LENGTH(d.name) + 1) / 1024
                FROM document d 
                JOIN collection c ON d.collection_id = c.id 
                WHERE c.project_id = ?1
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, projectId);
        Object result = nativeQuery.getSingleResult();
        return result != null ? ((Number) result).floatValue() : 0.0f;
    }

    public float countFieldKeysSize(long projectId) {
        String query = """
                SELECT SUM(LENGTH(k.name) + 1) / 1024
                FROM field_key k 
                JOIN document d ON k.document_id = d.id 
                JOIN collection c ON d.collection_id = c.id 
                WHERE c.project_id = ?1
                            """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, projectId);
        Object result = nativeQuery.getSingleResult();
        return result != null ? ((Number) result).floatValue() : 0.0f;

    }

    public float countFieldValueSize(long projectId) {
        String query = """
                SELECT SUM(LENGTH(COALESCE(v.key_name, '')) + LENGTH(COALESCE(v.value_name, '')) + 2) / 1024 
                FROM field_value v 
                JOIN field_key k ON v.field_key_id = k.id 
                JOIN document d ON k.document_id = d.id 
                JOIN collection c ON d.collection_id = c.id 
                WHERE c.project_id = ?1
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, projectId);

        Object result = nativeQuery.getSingleResult();
        return result != null ? ((Number) result).floatValue() : 0.0f;
    }

    public long countDocumentsByProjectId(long projectId) {
        String query = """
                SELECT COUNT(*)
                FROM document d
                JOIN collection c ON d.collection_id = c.id 
                WHERE c.project_id = ?1 
                """;
        Query nativeQuery = entityManager.createNativeQuery(query);
        nativeQuery.setParameter(1, projectId);
        return (long)nativeQuery.getSingleResult();
    }
}

