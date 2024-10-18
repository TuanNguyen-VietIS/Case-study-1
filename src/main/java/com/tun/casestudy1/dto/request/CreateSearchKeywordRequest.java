package com.tun.casestudy1.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSearchKeywordRequest {
    String keyword;

    String matchKeyword;

    String platform;

    String device;

    String matchingPattern;
}
