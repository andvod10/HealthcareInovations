package com.avinty.hr.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
