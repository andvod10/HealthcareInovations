package com.avinty.hr.data.repository;

import com.avinty.hr.data.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String> {
    @Override
    @Query(
            "select e from Employee e " +
                    "left join fetch e.createdBy " +
                    "left join fetch e.updatedBy " +
                    "left join fetch e.department "
    )
    List<Employee> findAll();

    @Query(
            "select e from Employee e " +
                    "left join fetch e.createdBy " +
                    "left join fetch e.updatedBy " +
                    "left join fetch e.department " +
                    "where e.id = :id"
    )
    Optional<Employee> findByIdFetch(@Param("id") String id);
}
