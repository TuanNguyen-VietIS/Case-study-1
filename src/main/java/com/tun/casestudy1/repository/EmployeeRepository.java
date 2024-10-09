package com.tun.casestudy1.repository;

import com.tun.casestudy1.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee>{
    Optional<Employee> findByEmail(String email);

    List<Employee> findAllByDepartmentId(int id);

}
