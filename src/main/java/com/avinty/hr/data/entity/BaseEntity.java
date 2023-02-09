package com.avinty.hr.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Audited
@AuditOverride(forClass = AbstractAuditable.class)
@MappedSuperclass
public abstract class BaseEntity extends AbstractAuditable<Employee> implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;
}
