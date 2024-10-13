package com.tun.casestudy1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ExcellentEmployeeResponse {
    int employeeId;
    String name;
    String imageUrl;
    String nameOfDept;
    Long totalAchievements;
}
