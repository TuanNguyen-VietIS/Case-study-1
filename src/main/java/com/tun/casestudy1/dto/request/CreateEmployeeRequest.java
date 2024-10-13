package com.tun.casestudy1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateEmployeeRequest {

    String name;

    int gender;

    String imageUrl;

    LocalDate dateOfBirth;

    @Min(value = 0, message = "error.salary")
    int salary;

    @Min(value = 1, message = "error.level")
    @Max(value = 10, message = "error.level")
    int level;

    @Email(message = "error.email")
    String email;

    String password;

    @Pattern(regexp = "\\d{10}", message = "error.phoneNumber")
    String phoneNumber;

    String note;

    Integer departmentId;
}
