package com.tun.casestudy1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmployeeAchievementDto {
    int employeeId;
    String name;
    int totalAchievements;
    int totalDisciplinary;
    int rewardPoints;
}
