package com.tun.casestudy1.mapper;

import com.tun.casestudy1.dto.request.CreateSearchResultRequest;
import com.tun.casestudy1.dto.response.SearchResultResponse;
import com.tun.casestudy1.entity.SearchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SearchResultMapper {
    SearchResult toSearchResult(CreateSearchResultRequest request);

    @Mapping(target="suggestions", ignore = true)
    SearchResultResponse toSearchResultResponse(SearchResult searchResult);
}
