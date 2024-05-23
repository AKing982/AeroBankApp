package com.example.aerobankapp.workbench.sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

import java.util.List;

public class QueryUtil {

    private QueryUtil(){

    }

    public static <T> CriteriaQuery<T> createCriteriaQuery(Class<T> resultClass, EntityManager entityManager) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        return cb.createQuery(resultClass);
    }

    public static <T> Root<T> createRoot(CriteriaQuery<?> query, Class<T> entityClass) {
        return query.from(entityClass);
    }

    public static <T, J> Join<T, J> createJoin(Root<T> root, String attributeName) {
        return root.join(attributeName);
    }

    public static <T, J> Join<T, J> createJoin(Join<?, T> join, String attributeName) {
        return join.join(attributeName);
    }

    public static <T> Predicate createEqualityPredicate(CriteriaBuilder cb, Path<T> path, T value) {
        return cb.equal(path, value);
    }

    public static <T> List<T> executeQuery(EntityManager entityManager, CriteriaQuery<T> query) {
        return entityManager.createQuery(query).getResultList();
    }
}
