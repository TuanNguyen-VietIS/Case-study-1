package com.tun.casestudy1.dto.request;

import com.tun.casestudy1.entity.Department;
import com.tun.casestudy1.entity.EmployeeRecord;
import com.tun.casestudy1.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEmployeeDto {
    String name;

    @Min(value = 0, message = "error.salary")
    int salary;

    @Min(value = 1, message = "error.level")
    @Max(value = 10, message = "error.level")
    int level;

    @Pattern(regexp = "\\d{10}", message = "error.phoneNumber")
    String phoneNumber;

    Integer departmentId;

    String imageUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dOB;

    @AssertTrue(message = "error.dOB")
    public boolean isValidDOB() {
        return dOB != null && !dOB.isBefore(LocalDate.of(1950, 1, 1)) && !dOB.isAfter(LocalDate.of(2024, 12, 31));
    }
}
