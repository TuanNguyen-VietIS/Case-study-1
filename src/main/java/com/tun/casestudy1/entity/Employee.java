package com.tun.casestudy1.entity;

import com.tun.casestudy1.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    int gender;

    String imageUrl;

    LocalDate dOB;

    int salary;

    int level;

    String email;

    String password;

    String phoneNumber;

    String note;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    Role role = Role.USER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    Department department;

    @Column(name = "department_id")
    Integer departmentId;

    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<EmployeeRecord> employeeRecords;
}
