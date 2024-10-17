package com.tun.casestudy1.utility;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Component
public class ScreenshotUtil {
    public String capture(WebDriver driver, String fileName) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String screenshotPath = "screenshots/" + fileName + ".png";
        String destinationPath = "src/main/resources/static/" + screenshotPath;


        File destination = new File(destinationPath);

        try {
            File directory = new File(destination.getParent());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            ImageIO.write(ImageIO.read(screenshot), "png", destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return screenshotPath;
    }
}
