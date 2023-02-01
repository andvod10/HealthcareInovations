package com.avinty.hr.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "account_role")
    private AccountRoles accountRole;
    @Column(name = "is_active")
    private boolean isActive = false;
    @JoinColumn(name = "dep_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
}
