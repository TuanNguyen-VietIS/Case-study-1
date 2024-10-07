package com.tun.casestudy1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExcellentEmployeeDto {
    int employeeId;
    String name;
    String imageUrl;
    String nameOfDept;
    int totalAchievements;
}
