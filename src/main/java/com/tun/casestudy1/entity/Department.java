package com.tun.casestudy1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;

    @OneToMany(mappedBy = "department",cascade = {CascadeType.MERGE, CascadeType.PERSIST},fetch = FetchType.LAZY)
    List<Employee> employees;

    @PreRemove
    private void preRemove() {
        for (Employee employee : employees) {
            employee.setDepartmentId(null);
        }
    }
}
