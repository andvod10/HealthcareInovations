package com.avinty.hr.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
@Table(name = "token")
public class Token extends BaseEntity {
    @Column(name = "access_token", length = 1024)
    private String accessToken;
    @Column(name = "refresh_token", length = 1024)
    private String refreshToken;
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Employee employee;
}
