package com.avinty.hr.data.repository;

import com.avinty.hr.data.entity.Employee;
import com.avinty.hr.data.entity.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {
    Optional<Token> findByEmployee(Employee employee);

    @Query("SELECT t FROM Token t " +
            "LEFT JOIN FETCH t.employee e " +
            "WHERE e.id = :employeeId " +
            "AND ( t.accessToken = :token " +
            "OR t.refreshToken = :token ) ")
    Optional<Token> findToken(@Param("employeeId") String employeeId, @Param("token") String token);
}
