package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.dto.request.CreateSearchResultRequest;
import com.tun.casestudy1.dto.response.SearchResultResponse;
import com.tun.casestudy1.entity.SearchResult;
import com.tun.casestudy1.mapper.SearchResultMapper;
import com.tun.casestudy1.repository.SearchResultRepository;
import com.tun.casestudy1.service.SearchResultService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchResultServiceImpl implements SearchResultService {
    SearchResultRepository searchResultRepository;
    SearchResultMapper searchResultMapper;

    @Override
    public SearchResultResponse save(CreateSearchResultRequest request) {
        SearchResult searchResult = searchResultMapper.toSearchResult(request);
        searchResultRepository.save(searchResult);
        return searchResultMapper.toSearchResultResponse(searchResult);
    }
}
