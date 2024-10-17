package com.tun.casestudy1.service.impl;

import com.tun.casestudy1.dto.request.CreateSearchResultRequest;
import com.tun.casestudy1.dto.response.SearchResultResponse;
import com.tun.casestudy1.service.SearchResultService;
import com.tun.casestudy1.utility.ScreenshotUtil;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SeleniumService {

    private WebDriver webDriver;

    private final ScreenshotUtil screenshotUtil;
    private final SearchResultService searchResultService;

    public SearchResultResponse searchOnPlatform(String inputKeyword, String matchKeyword, String platform,
                                         String device, String matchingPattern, int searchKeywordId) {

        webDriver = createWebDriver(device);

        navigateToPlatform(platform);

        inputSearchKeyword(inputKeyword, platform);

        List<WebElement> suggestions = getSuggestionsByPlatform(platform);

        List<String> suggestionTexts = extractTextFromSuggestions(suggestions);

        boolean found = matchKeywords(suggestionTexts, inputKeyword, matchKeyword, matchingPattern);

        String screenshotPath = captureScreenshot(inputKeyword);

        CreateSearchResultRequest request = CreateSearchResultRequest.builder()
                .searchKeywordId(searchKeywordId)
                .searchDate(LocalDate.now())
                .screenshotPath(screenshotPath)
                .suggestions(String.join("<br>", suggestionTexts))
                .found(found)
                .build();

        return searchResultService.save(request);
    }

    private String captureScreenshot(String inputKeyword) {
        String screenshotFileName = LocalDate.now() + "_" + inputKeyword;
        return screenshotUtil.capture(webDriver, screenshotFileName);
    }

    private void navigateToPlatform(String platform) {
        String url = getSearchUrl(platform);
        webDriver.get(url);
//        setDeviceView(device);
    }

    private void inputSearchKeyword(String inputKeyword, String platform) {
        WebElement searchBox;

        if ("google".equalsIgnoreCase(platform)) {
            searchBox = webDriver.findElement(By.name("q"));
        } else if ("yahoo".equalsIgnoreCase(platform)) {
            searchBox = webDriver.findElement(By.name("p"));
        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        searchBox.sendKeys(inputKeyword);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<WebElement> getSuggestionsByPlatform(String platform) {
        if ("google".equalsIgnoreCase(platform)) {
//            return webDriver.findElements(By.cssSelector("ul[role='listbox'] li div.wM6W7d span"));
            return webDriver.findElements(By.cssSelector("ul[role='listbox'] li"));
        } else if ("yahoo".equalsIgnoreCase(platform)) {
            return webDriver.findElements(By.cssSelector("ul[role='listbox'] li span"));
        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    private List<String> extractTextFromSuggestions(List<WebElement> suggestions) {
        List<String> suggestionTexts = new ArrayList<>();
        for (WebElement suggestion : suggestions) {
            String text = suggestion.getText().trim();
            if (!text.isEmpty()) {
                suggestionTexts.add(text);
            }
        }
        return suggestionTexts;
    }

    private boolean matchKeywords(List<String> suggestionTexts, String inputKeyword, String matchKeyword, String matchingPattern) {
        String combinedKeywords = inputKeyword + " " + matchKeyword;
        for (String suggestion : suggestionTexts) {
            String suggestionText = suggestion.toLowerCase();
            if ("partial".equalsIgnoreCase(matchingPattern)) {
                if (suggestionText.contains(inputKeyword.toLowerCase()) && suggestionText.contains(matchKeyword.toLowerCase())) {
                    return true;
                }
            } else if ("exact".equalsIgnoreCase(matchingPattern)) {
                if (suggestionText.contains(combinedKeywords.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getSearchUrl(String platform) {
        if ("google".equalsIgnoreCase(platform)) {
            return "https://www.google.com/";
        } else if ("yahoo".equalsIgnoreCase(platform)) {
            return "https://search.yahoo.com/";
        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    private WebDriver createWebDriver(String device) {
        ChromeOptions options = new ChromeOptions();

        if ("smartphone".equalsIgnoreCase(device)) {
            Map<String, Object> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", "Nexus 5");
            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }

        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        return new ChromeDriver(options);
    }

    public String captureScreenshot() {
        String screenshotPath = screenshotUtil.capture(webDriver, LocalDate.now().toString());
        return "/static/screenshots/" + screenshotPath;
    }

    @PreDestroy
    public void cleanUp() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
}
