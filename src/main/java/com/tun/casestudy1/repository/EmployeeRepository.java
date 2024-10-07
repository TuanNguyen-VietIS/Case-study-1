package com.tun.casestudy1.repository;

import com.tun.casestudy1.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
    Optional<Employee> findByEmail(String email);

    @Query(value = "SELECT e.* FROM employee e " +
            "LEFT JOIN department d ON e.department_id = d.id " +
            "WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR e.phone_number LIKE CONCAT('%', :searchValue, '%') " +
            "OR LOWER(d.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR e.salary LIKE CONCAT('%', :searchValue, '%')" +
            "OR e.level LIKE CONCAT('%', :searchValue, '%')" +
            "OR DATE_FORMAT(e.dOB, '%d/%m/%Y') LIKE CONCAT('%', :searchValue, '%')", nativeQuery = true)
    List<Employee> searchByQuery(@Param("searchValue") String searchValue);
}
