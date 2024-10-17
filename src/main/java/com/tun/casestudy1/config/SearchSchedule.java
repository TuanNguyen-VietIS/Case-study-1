package com.tun.casestudy1.config;

import com.tun.casestudy1.entity.SearchKeyword;
import com.tun.casestudy1.repository.SearchKeywordRepository;
import com.tun.casestudy1.service.impl.SeleniumService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchSchedule {

    SearchKeywordRepository searchKeywordRepository;
    SeleniumService seleniumService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void performDailySearch() {
        List<SearchKeyword> keywords = searchKeywordRepository.findAll();

        for (SearchKeyword keyword : keywords) {
            seleniumService.searchOnPlatform(
                    keyword.getKeyword(),
                    keyword.getMatchKeyword(),
                    keyword.getPlatform(),
                    keyword.getDevice(),
                    keyword.getMatchingPattern(),
                    keyword.getId()
            );
        }
    }
}
