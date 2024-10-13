package com.tun.casestudy1.dto.response;

import com.tun.casestudy1.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmployeeResponse {
    int id;

    String name;

    String imageUrl;

    LocalDate dateOfBirth;

    int salary;

    int level;

    String phoneNumber;

    Role role;

    Integer departmentId;

    String departmentName;
}
