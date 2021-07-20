package com.ozdemirkan.store.product.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class BaseEntity
{
    @Id
    private String id;
    private Date createDate;
    private Date updateDate;

    BaseEntity() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        return getId().equals(other.getId());
    }

    @PrePersist
    void createDate(){
        this.createDate = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    void updateDate(){
        this.updateDate = Timestamp.valueOf(LocalDateTime.now());
    }

}