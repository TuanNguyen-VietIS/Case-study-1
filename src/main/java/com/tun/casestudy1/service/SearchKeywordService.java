package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.CreateSearchKeywordRequest;
import com.tun.casestudy1.dto.response.SearchKeywordResponse;
import com.tun.casestudy1.dto.response.SearchKeywordWithResultsResponse;

import java.util.List;

public interface SearchKeywordService {

    void save(CreateSearchKeywordRequest dto);

    List<SearchKeywordResponse> findAll();

    SearchKeywordResponse find(int id);

    List<SearchKeywordWithResultsResponse> getKeywordsWithResultsByMonthAndYear(int month, int year);
}
