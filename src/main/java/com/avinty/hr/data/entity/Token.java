package com.avinty.hr.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "token")
public class Token extends BaseEntity {
    @Column(name = "access_token", length = 1024)
    private String accessToken;
    @Column(name = "refresh_token", length = 1024)
    private String refreshToken;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;
}
