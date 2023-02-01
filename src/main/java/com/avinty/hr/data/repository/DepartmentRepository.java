package com.avinty.hr.data.repository;

import com.avinty.hr.data.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    List<Department> findByName(String name);

    @Query(
            "select d from Department d " +
                    "left join fetch d.manager " +
                    "left join fetch d.employees "
    )
    List<Department> findAllByAndFetchEmployees();

    @Query(
            "select d from Department d " +
                    "left join fetch d.createdBy " +
                    "left join fetch d.lastModifiedBy " +
                    "left join fetch d.manager " +
                    "left join fetch d.employees " +
                    "where d.id = :id"
    )
    Optional<Department> findByIdFetch(@Param("id") String id);
}
