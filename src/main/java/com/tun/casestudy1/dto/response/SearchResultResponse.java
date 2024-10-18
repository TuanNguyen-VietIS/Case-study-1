package com.tun.casestudy1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SearchResultResponse {
    int id;

    String stringFound;

    List<String> suggestions;

    int found;

    String screenshotPath;

    LocalDate searchDate;

    int searchKeywordId;
}
