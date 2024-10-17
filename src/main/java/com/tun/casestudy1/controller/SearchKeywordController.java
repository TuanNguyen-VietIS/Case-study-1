package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.request.CreateSearchKeywordRequest;
import com.tun.casestudy1.dto.response.SearchKeywordResponse;
import com.tun.casestudy1.dto.response.SearchKeywordWithResultsResponse;
import com.tun.casestudy1.service.SearchKeywordService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/keywords")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SearchKeywordController {

    SearchKeywordService searchKeywordService;

    @GetMapping
    public String getSearchKeywordPage(Model model) {
        List<SearchKeywordWithResultsResponse> keywordResponses = searchKeywordService.getAllKeywordsWithResults();
        model.addAttribute("keywords", keywordResponses);
        return "admin/keyword/view-list";
    }

    @PostMapping("/add-keyword")
    public String addKeyword(@ModelAttribute CreateSearchKeywordRequest dto) {
        searchKeywordService.save(dto);
        return "redirect:/admin/keywords";
    }

}
