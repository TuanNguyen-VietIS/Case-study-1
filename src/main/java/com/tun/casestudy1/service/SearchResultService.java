package com.tun.casestudy1.service;

import com.tun.casestudy1.dto.request.CreateSearchResultRequest;
import com.tun.casestudy1.dto.response.SearchResultResponse;

public interface SearchResultService {
    SearchResultResponse save(CreateSearchResultRequest request);
}
