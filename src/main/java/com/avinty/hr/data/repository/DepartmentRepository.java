package com.avinty.hr.data.repository;

import com.avinty.hr.data.entity.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, String> {
    List<Department> findByName(String name);

    @Query(
            "select d from Department d " +
                    "left join fetch d.createdBy " +
                    "left join fetch d.updatedBy " +
                    "left join fetch d.manager " +
                    "left join fetch d.employees "
    )
    List<Department> findAllByAndFetchEmployees();

    @Query(
            "select d from Department d " +
                    "left join fetch d.createdBy " +
                    "left join fetch d.updatedBy " +
                    "left join fetch d.manager " +
                    "left join fetch d.employees " +
                    "where d.id = :id"
    )
    Optional<Department> findByIdFetchAll(@Param("id") String id);
}
