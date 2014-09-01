package com.zyd.core.database;

import com.zyd.core.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 */
public class JPAAccess {
    private final Logger logger = LoggerFactory.getLogger(JPAAccess.class);

    private EntityManager entityManager;

    public <T> T get(Class<T> entityClass, Object id) {
        StopWatch watch = new StopWatch();
        try {
            return entityManager.find(entityClass, id);
        } finally {
            logger.debug("get, entityClass={}, id={}, elapsedTime={}", entityClass.getName(), id, watch.elapsedTime());
        }
    }

    public void save(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.persist(entity);
        } finally {
            logger.debug("save, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void update(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.merge(entity);
        } finally {
            logger.debug("update, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void delete(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.remove(entity);
        } finally {
            logger.debug("delete, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void refresh(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.refresh(entity);
        } finally {
            logger.debug("refresh, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public void detach(Object entity) {
        StopWatch watch = new StopWatch();
        try {
            entityManager.detach(entity);
        } finally {
            logger.debug("detach, entityClass={}, elapsedTime={}", entity.getClass().getName(), watch.elapsedTime());
        }
    }

    public <T> List<T> find(CriteriaQuery<T> query) {
        StopWatch watch = new StopWatch();
        try {
            return entityManager.createQuery(query).getResultList();
        } finally {
            logger.debug("find by CriteriaQuery<T>, elapsedTime={}", watch.elapsedTime());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(String queryString, Map<String, Object> params) {
        StopWatch watch = new StopWatch();
        try {
            Query query = entityManager.createQuery(queryString);
            if (params != null)
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            return query.getResultList();
        } finally {
            logger.debug("find, query={}, params={}, elapsedTime={}", queryString, params, watch.elapsedTime());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> find(String queryString, Map<String, Object> params, int offset, int fetchSize) {
        StopWatch watch = new StopWatch();
        try {
            Query query = entityManager.createQuery(queryString);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            query.setFirstResult(offset);
            query.setMaxResults(fetchSize);
            return query.getResultList();
        } finally {
            logger.debug("find, query={}, params={},offset={},fetchSize={}, elapsedTime={}", queryString, params, offset, fetchSize, watch.elapsedTime());
        }
    }

    public <T> List<T> find(CriteriaQuery<T> query, int offset, int fetchSize) {
        StopWatch watch = new StopWatch();
        try {
            TypedQuery<T> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(fetchSize);
            return typedQuery.getResultList();
        } finally {
            logger.debug("find by CriteriaQuery<T>,offset={},fetchSize={}, elapsedTime={}", offset, fetchSize, watch.elapsedTime());
        }
    }


    public <T> T findUniqueResult(CriteriaQuery<T> query) {
        StopWatch watch = new StopWatch();
        try {
            List<T> results = entityManager.createQuery(query).getResultList();
            return getUniqueResult(results);
        } finally {
            logger.debug("findUniqueResult by CriteriaQuery<T>, elapsedTime={}", watch.elapsedTime());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T findUniqueResult(String queryString, Map<String, Object> params) {
        StopWatch watch = new StopWatch();
        try {
            Query query = entityManager.createQuery(queryString);
            if (params != null)
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            List<T> results = query.getResultList();
            return getUniqueResult(results);
        } finally {
            logger.debug("findUniqueResult, query={}, params={}, elapsedTime={}", queryString, params, watch.elapsedTime());
        }
    }

    private <T> T getUniqueResult(List<T> results) {
        if (results.isEmpty()) return null;
        if (results.size() > 1) {
            throw new NonUniqueResultException("result returned more than one element, returnedSize=" + results.size());
        }
        return results.get(0);
    }

    public int update(String queryString, Map<String, Object> params) {
        StopWatch watch = new StopWatch();
        try {
            Query query = entityManager.createQuery(queryString);
            if (params != null)
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            return query.executeUpdate();
        } finally {
            logger.debug("update, query={}, params={}, elapsedTime={}", queryString, params, watch.elapsedTime());
        }
    }

    public CriteriaBuilder criteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
