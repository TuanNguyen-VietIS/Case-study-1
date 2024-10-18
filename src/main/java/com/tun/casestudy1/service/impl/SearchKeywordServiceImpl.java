package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.dto.request.CreateSearchKeywordRequest;
import com.tun.casestudy1.dto.response.SearchKeywordResponse;
import com.tun.casestudy1.dto.response.SearchKeywordWithResultsResponse;
import com.tun.casestudy1.dto.response.SearchResultResponse;
import com.tun.casestudy1.entity.SearchKeyword;
import com.tun.casestudy1.exception.AppException;
import com.tun.casestudy1.exception.ErrorCode;
import com.tun.casestudy1.mapper.SearchKeywordMapper;
import com.tun.casestudy1.mapper.SearchResultMapper;
import com.tun.casestudy1.repository.SearchKeywordRepository;
import com.tun.casestudy1.service.SearchKeywordService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchKeywordServiceImpl implements SearchKeywordService {

    SearchKeywordRepository searchKeywordRepository;
    SearchKeywordMapper searchKeywordMapper;
    SearchResultMapper searchResultMapper;

    @Override
    public void save(CreateSearchKeywordRequest dto) {
        searchKeywordRepository.save(searchKeywordMapper.toSearchKeyword(dto));
    }

    @Override
    public List<SearchKeywordResponse> findAll() {
        List<SearchKeyword> searchKeywords = searchKeywordRepository.findAll();
        return searchKeywords.stream().map(searchKeywordMapper::toSearchKeywordResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SearchKeywordResponse find(int id) {
        SearchKeyword searchKeyword = searchKeywordRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED));
        return searchKeywordMapper.toSearchKeywordResponse(searchKeyword);
    }
    @Override
    public List<SearchKeywordWithResultsResponse> getKeywordsWithResultsByMonthAndYear(int month, int year) {
        List<SearchKeyword> searchKeywords = searchKeywordRepository.findAll();
        return searchKeywords.stream()
                .map(searchKeyword -> convertToSearchKeywordWithResultsResponse(searchKeyword, month, year))
                .collect(Collectors.toList());
    }

    private SearchKeywordWithResultsResponse convertToSearchKeywordWithResultsResponse(SearchKeyword searchKeyword, int month, int year) {
        List<SearchResultResponse> searchResults = searchKeyword.getSearchResults()
                .stream()
                .filter(searchResult -> {
                    // Lọc các kết quả theo tháng và năm của searchDate
                    LocalDate searchDate = searchResult.getSearchDate();
                    return searchDate.getMonthValue() == month && searchDate.getYear() == year;
                })
                .map(searchResult -> {
                    SearchResultResponse searchResultResponse = searchResultMapper.toSearchResultResponse(searchResult);
                    String stringSuggestions = searchResult.getSuggestions();
                    List<String> listSuggestions = new ArrayList<>();

                    String[] items = stringSuggestions.split("<br>");
                    for (String item : items) {
                        listSuggestions.add(item.trim());
                    }

                    searchResultResponse.setSuggestions(listSuggestions);
                    return searchResultResponse;
                })
                .collect(Collectors.toList());

        return SearchKeywordWithResultsResponse.builder()
                .id(searchKeyword.getId())
                .keyword(searchKeyword.getKeyword())
                .matchKeyword(searchKeyword.getMatchKeyword())
                .platform(searchKeyword.getPlatform())
                .device(searchKeyword.getDevice())
                .matchingPattern(searchKeyword.getMatchingPattern())
                .searchResults(searchResults)
                .build();
    }
}
