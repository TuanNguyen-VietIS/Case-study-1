package com.tun.casestudy1.config;

import com.tun.casestudy1.entity.Employee;
import com.tun.casestudy1.enums.Role;
import com.tun.casestudy1.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class DataInitializer {

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    public void init() {
        if (employeeRepository.findByEmail("admin@gmail.com").isEmpty()) {
            Employee admin = Employee.builder()
                    .name("Admin")
                    .email("admin@gmail.com")
                    .password("12345")
                    .role(Role.ADMIN)
                    .dOB(LocalDate.of(1990, 1, 1))
                    .gender(1)
                    .salary(1000000)
                    .level(10)
                    .phoneNumber("0123456789")
                    .note("Admin account")
                    .departmentId(5)
                    .build();
            employeeRepository.save(admin);
        }
    }
}
