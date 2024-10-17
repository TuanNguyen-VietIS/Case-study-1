package com.tun.casestudy1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SearchResultResponse {
    int id;

    String suggestions;

    boolean found;

    String screenshotPath;

    LocalDate searchDate;

    int searchKeywordId;
}
