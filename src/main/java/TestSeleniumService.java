//import com.tun.casestudy1.dto.response.SearchResultResponse;
//import com.tun.casestudy1.service.impl.SeleniumService;
//import com.tun.casestudy1.utility.ScreenshotUtil;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//public class TestSeleniumService {
//    public static void main(String[] args) {
//        // Setup WebDriver (ví dụ sử dụng ChromeDriver)
////        System.setProperty("webdriver.chrome.driver", "C:/chromedriver/chromedriver.exe"); // Đường dẫn đến chromedriver
//
//        WebDriverManager.chromedriver().setup();
//
//        // Khởi tạo ChromeOptions để cài đặt cấu hình (nếu cần)
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--no-sandbox");
//        options.addArguments("--disable-dev-shm-usage");
//
//        WebDriver webDriver = new ChromeDriver(options);
//
//        ScreenshotUtil screenshotUtil = new ScreenshotUtil();
//
//        SeleniumService seleniumService = new SeleniumService(webDriver, screenshotUtil);
//
//        String inputSearch = "GAM Esport";
//        String matchKeyword = "2024";
//        String platform = "google";
//        String device = "PC";
//        String matchingPattern = "exact";
//
//        SearchResultResponse results = seleniumService.searchOnPlatform(inputSearch, matchKeyword, platform, device, matchingPattern);
//
//        System.out.println("Danh sách gợi ý tìm kiếm:");
//        for (String result : results.getSuggestions()) {
//            System.out.println(result);
//        }
//        System.out.println("Trạng thái tìm kiếm: ");
//        System.out.println(results.getFound());
//
//        String screenshotPath = seleniumService.captureScreenshot();
//        System.out.println("Ảnh chụp màn hình được lưu tại: " + screenshotPath);
//
//        seleniumService.closeWebDriver();
//    }
//}
