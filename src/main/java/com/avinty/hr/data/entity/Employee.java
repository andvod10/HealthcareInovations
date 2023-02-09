package com.avinty.hr.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@Entity
@DynamicUpdate
@Table(name = "employee")
public class Employee extends BaseEntity {
    @Column(name = "email", unique = true)
    private String email;
    @ToString.Exclude
    @Column(name = "password")
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "account_role")
    private AccountRoles accountRole;
    @Column(name = "is_active")
    private boolean isActive = false;
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "dep_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
}
