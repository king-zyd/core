package com.suryani.rest.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author neo
 */
@Entity
public class JPATestEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        JPATestEntity that = (JPATestEntity) object;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
