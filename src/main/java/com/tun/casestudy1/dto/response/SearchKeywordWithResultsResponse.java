package com.tun.casestudy1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchKeywordWithResultsResponse {
    int id;

    String keyword;

    String matchKeyword;

    String platform;

    String device;

    String matchingPattern;

    List<SearchResultResponse> searchResults;
}