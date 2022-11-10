package com.avinty.hr.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RsDepartment {
    String id;
    String createdBy;
    String createdAt;
    String updatedBy;
    String updatedAt;
    String name;
    String managerId;
    List<RsEmployee> employees;
}
