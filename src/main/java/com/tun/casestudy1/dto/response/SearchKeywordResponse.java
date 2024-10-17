package com.tun.casestudy1.dto.response;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchKeywordResponse {
    int id;

    String keyword;

    String matchKeyword;

    String platform;

    String device;

    String matchingPattern;
}
