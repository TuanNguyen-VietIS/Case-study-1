package com.tun.casestudy1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class StringFoundResponse {
    int found;
    String stringFound;
}
