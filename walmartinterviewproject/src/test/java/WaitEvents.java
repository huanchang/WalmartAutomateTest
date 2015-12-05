/**
 * Created by huanchang on 12/4/15.
 *
 */

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitEvents {
    private static final Logger logger = Logger.getLogger(WaitEvents.class);
    private static final int DEFAULT_WEBDRIVER_WAITTIME_SECOND = 10;

    public static void waitForInvisibilityOfElement(WebDriver driver, By by) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WEBDRIVER_WAITTIME_SECOND);
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            logger.error("Timeout for waiting the invisibility by:"+by.toString());
        }
    }

    public static boolean waitForVisibilityOfElement(WebDriver driver, By by) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WEBDRIVER_WAITTIME_SECOND);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            logger.error("Timeout for waiting the visibility by:"+by.toString());
            return false;
        }
        return true;
    }

    public static boolean waitForVisibilityOfElement(WebDriver driver, WebElement webelement) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WEBDRIVER_WAITTIME_SECOND);
        try {
            wait.until(ExpectedConditions.visibilityOf(webelement));
        } catch (TimeoutException e) {
            logger.error("Timeout for waiting the visibility of web element.");
            return false;
        }
        return true;
    }


    public static void waitForStaleElement(WebDriver driver, WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WEBDRIVER_WAITTIME_SECOND);
        try {
            wait.until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            logger.error("Timeout for waiting element reference to be stale.");
        }
    }

}
