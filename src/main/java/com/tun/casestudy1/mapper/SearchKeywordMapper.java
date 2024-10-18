package com.tun.casestudy1.mapper;

import com.tun.casestudy1.dto.request.CreateSearchKeywordRequest;
import com.tun.casestudy1.dto.response.SearchKeywordResponse;
import com.tun.casestudy1.entity.SearchKeyword;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SearchKeywordMapper {
    SearchKeyword toSearchKeyword(CreateSearchKeywordRequest dto);

    SearchKeywordResponse toSearchKeywordResponse(SearchKeyword searchKeyword);
}
