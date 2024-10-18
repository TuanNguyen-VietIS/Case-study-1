package com.tun.casestudy1.controller;

import com.tun.casestudy1.dto.request.CreateSearchKeywordRequest;
import com.tun.casestudy1.dto.response.SearchKeywordWithResultsResponse;
import com.tun.casestudy1.service.SearchKeywordService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
@RequestMapping("/admin/keywords")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class SearchKeywordController {

    SearchKeywordService searchKeywordService;

    @GetMapping
    public String getSearchKeywordPage(Model model) {
        List<SearchKeywordWithResultsResponse> keywordResponses;
        LocalDate currentDate = LocalDate.now();
        keywordResponses = searchKeywordService.getKeywordsWithResultsByMonthAndYear(currentDate.getMonthValue(), currentDate.getYear());
        model.addAttribute("keywords", keywordResponses);
        return "admin/keyword/view-list";
    }

    @GetMapping("/list-captcha")
    public String getKeywordCaptchaPage(Model model) {
        List<SearchKeywordWithResultsResponse> keywordResponses;
        LocalDate currentDate = LocalDate.now();
        keywordResponses = searchKeywordService.getKeywordsWithResultsByMonthAndYear(currentDate.getMonthValue(), currentDate.getYear());
        model.addAttribute("keywords", keywordResponses);
        return "admin/keyword/view-list-captcha";
    }

    @GetMapping({"/table", "/list-captcha/table"})
    public String getSearchKeywordPage(@RequestParam(value = "date", required = false) String date,
                                       String mode,
                                       Model model) {
        List<SearchKeywordWithResultsResponse> keywordResponses;
        LocalDate selectedDate;

        if (date != null && !date.isEmpty()) {
            selectedDate = LocalDate.parse(date + "-01"); // Chỉ cần ngày đầu tháng
            keywordResponses = searchKeywordService.getKeywordsWithResultsByMonthAndYear(selectedDate.getMonthValue(), selectedDate.getYear());
        } else {
            selectedDate = LocalDate.now();
            keywordResponses = searchKeywordService.getKeywordsWithResultsByMonthAndYear(selectedDate.getMonthValue(), selectedDate.getYear());
        }

        int daysInMonth = YearMonth.from(selectedDate).lengthOfMonth();
        model.addAttribute("keywords", keywordResponses);
        model.addAttribute("daysInMonth", daysInMonth);
        return mode.equals("keyword") ? "fragments/keyword-table-body" : "fragments/captcha-table-body";
    }

    @GetMapping("/add-keyword")
    public String getAddKeywordPage() {
        return "admin/keyword/add";
    }

    @PostMapping("/add-keyword")
    public String addKeyword(@ModelAttribute CreateSearchKeywordRequest dto) {
        searchKeywordService.save(dto);
        return "redirect:/admin/keywords";
    }

}
