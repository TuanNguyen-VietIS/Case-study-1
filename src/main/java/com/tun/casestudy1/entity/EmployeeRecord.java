package com.tun.casestudy1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int type;

    String reason;

    LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    Employee employee;

    @Column(name = "employee_id")
    int employeeId;
}
