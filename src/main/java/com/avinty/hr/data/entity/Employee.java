package com.avinty.hr.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends BaseEntity {
    @Column(name = "email")
    String email;
    @Column(name = "password")
    String password;
    @Column(name = "full_name")
    String fullName;
    @JoinColumn(name = "dep_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    Department department;
}
