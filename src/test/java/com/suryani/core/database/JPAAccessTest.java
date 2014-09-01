package com.zyd.core.database;

import com.zyd.core.SpringServiceTest;
import com.suryani.rest.database.JPATestEntity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author neo
 */
public class JPAAccessTest extends SpringServiceTest {
    @Inject
    JPAAccess jpaAccess;

    @Test
    @Transactional
    public void save() {
        JPATestEntity entity = createEntity("someName");
        Assert.assertNotNull(entity.getId());
    }

    @Test
    @Transactional
    public void get() {
        JPATestEntity entity = createEntity("someName");
        JPATestEntity loadedEntity = jpaAccess.get(JPATestEntity.class, entity.getId());
        Assert.assertEquals("someName", loadedEntity.getName());
    }

    @Test
    @Transactional
    public void update() {
        JPATestEntity entity = createEntity("someName");
        entity.setName("someOtherName");
        jpaAccess.update(entity);
        JPATestEntity loadedEntity = jpaAccess.get(JPATestEntity.class, entity.getId());
        Assert.assertEquals("someOtherName", loadedEntity.getName());
    }

    @Test
    @Transactional
    public void find() {
        createEntity("someName");
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", "someName");
        List<JPATestEntity> results = jpaAccess.find("from " + JPATestEntity.class.getName() + " where name = :name", params);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("someName", results.get(0).getName());
    }

    @Test
    @Transactional
    public void findOffset() {
        createEntity("someName");
        createEntity("someName");
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", "someName");
        List<JPATestEntity> results = jpaAccess.find("from " + JPATestEntity.class.getName() + " where name = :name", params, 0, 2);

        Assert.assertEquals(2, results.size());

        results = jpaAccess.find("from " + JPATestEntity.class.getName() + " where name = :name", params, 1, 1);
        Assert.assertEquals(1, results.size());
    }


    @Test
    @Transactional
    public void findByCriteria() {
        createEntity("someName");

        CriteriaBuilder builder = jpaAccess.criteriaBuilder();
        CriteriaQuery<JPATestEntity> query = builder.createQuery(JPATestEntity.class);
        Root<JPATestEntity> from = query.from(JPATestEntity.class);
        query.where(builder.equal(from.get("name"), "someName"));
        List<JPATestEntity> results = jpaAccess.find(query);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("someName", results.get(0).getName());
    }

    @Test
    @Transactional
    public void findOffsetByCriteria() {
        createEntity("someName");
        createEntity("someName");

        CriteriaBuilder builder = jpaAccess.criteriaBuilder();
        CriteriaQuery<JPATestEntity> query = builder.createQuery(JPATestEntity.class);
        Root<JPATestEntity> from = query.from(JPATestEntity.class);
        query.where(builder.equal(from.get("name"), "someName"));
        List<JPATestEntity> results = jpaAccess.find(query, 0, 1);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals("someName", results.get(0).getName());

        results = jpaAccess.find(query, 0, 2);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals("someName", results.get(0).getName());

        results = jpaAccess.find(query, 0, 3);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals("someName", results.get(0).getName());
    }

    @Test
    @Transactional
    public void findUniqueResultReturnsNull() {
        JPATestEntity result = jpaAccess.findUniqueResult("from " + JPATestEntity.class.getName(), null);
        Assert.assertNull(result);
    }

    @Test
    @Transactional
    public void updateByQuery() {
        JPATestEntity entity = createEntity("someName");
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("name", "newName");
        jpaAccess.update("update JPATestEntity t SET t.name = :name where t.id = :id", params);

        JPATestEntity loadedEntity = jpaAccess.get(JPATestEntity.class, entity.getId());
        Assert.assertEquals("newName", loadedEntity.getName());
    }

    private JPATestEntity createEntity(String name) {
        JPATestEntity entity = new JPATestEntity();
        entity.setName(name);
        jpaAccess.save(entity);
        jpaAccess.detach(entity);
        return entity;
    }
}
