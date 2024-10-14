package com.tun.casestudy1.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
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

    @AssertTrue(message = "error.dOB")
    public boolean isValidDOB() {
        return dateOfBirth != null && !dateOfBirth.isBefore(LocalDate.of(1950, 1, 1)) &&
                !dateOfBirth.isAfter(LocalDate.of(2024, 12, 31));
    }
}
