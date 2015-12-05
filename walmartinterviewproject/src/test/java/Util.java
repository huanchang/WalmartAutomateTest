import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by huanchang on 12/3/15.
 *
 */
public class Util {

    public final static int WEBDRIVER_DEFAULT_WAITTIME_SECOND = 10;
    private static final int MAX_RETRYTIMES = 3;
    private final static Logger logger = Logger.getLogger("Util");
    private static final String NOT_CLICKABLE_EXCEPTION_PATTERN = ".* Element is not clickable.*";

    public static boolean sendKeyToTextField(WebDriver driver, By by, String textToSend) {
        logger.info("Send text:"+textToSend+" to:"+by.toString());

        WebDriverWait wait = new WebDriverWait(driver, Util.WEBDRIVER_DEFAULT_WAITTIME_SECOND);

        WebElement element = findElementBy(driver, by);

        if (element == null) {
            return false;
        } else {
            int retries = 0;

            while ( retries < MAX_RETRYTIMES) {

                try {
                    element.sendKeys(textToSend);
                    wait.until(ExpectedConditions.textToBePresentInElementValue(by, textToSend));
                    return true;
                } catch (TimeoutException e) {
                    if (retries < MAX_RETRYTIMES) {
                        logger.warn("Retried to send text to search field.");
                    } else {
                        logger.error("Timeout for waiting text to be presented in search field.");
                    }
                } catch (StaleElementReferenceException e) {
                    logger.error("Error: send text to stale element reference.");
                    return false;
                }
                retries++;
            }


            return false;
        }



    }

    public static boolean clickElement( WebDriver driver, WebElement element) {
        if (!WaitEvents.waitForVisibilityOfElement(driver, element)) {
            logger.error("Element is neither visible not to be clicked.");
            return false;
        }

        int retries = 1;

        while(retries <= MAX_RETRYTIMES) {
            try {
                element.click();
                logger.info("Clicked element.");
                return true;
            } catch (StaleElementReferenceException e) {
                logger.error(e.getMessage());
                break;
            } catch (NullPointerException e) {
                logger.error(e.getMessage());
                break;
            } catch (WebDriverException e) {
                if (e.getMessage().matches(NOT_CLICKABLE_EXCEPTION_PATTERN)) {
                    if (retries<MAX_RETRYTIMES) {
                        logger.warn("Retry to click element.");
                    }
                } else {
                    logger.error("Unexpected exception:" + e.getMessage());
                }
            }
            retries++;
        }

        logger.error("Element is not clickable.");

        return false;
    }

    public static boolean clickElement(WebDriver driver, By by) {
        WebDriverWait wait = new WebDriverWait(driver, WEBDRIVER_DEFAULT_WAITTIME_SECOND);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));

        } catch (TimeoutException e) {
            logger.error(e.getMessage());
        }
        return clickElement(driver, findElementBy(driver, by));
    }

    public static boolean clickElementByElement(WebDriver driver,WebElement parentElement, By childBy) {

        try {
            WebElement element = parentElement.findElement(childBy);
            clickElement(driver, element);
            return true;
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
        }

        return false;
    }

    public static WebElement findElementBy( WebDriver driver, By by) {

        WebDriverWait wait = new WebDriverWait(driver, WEBDRIVER_DEFAULT_WAITTIME_SECOND);
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            logger.error("Timeout for waiting the visibility of web element by: "+by.toString());
        }
        return null;
    }

    public static List<WebElement> findElementsBy( WebDriver driver, By by) {

        WebDriverWait wait = new WebDriverWait(driver, WEBDRIVER_DEFAULT_WAITTIME_SECOND);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return driver.findElements(by);

        } catch (TimeoutException e) {
            logger.error("Timeout for waiting the visibility of web element by: "+by.toString());
        }
        return null;
    }

    public static WebElement findElementByElement( WebElement ancestorElement, By by) {
        try {
            return ancestorElement.findElement(by);
        } catch (NoSuchElementException e) {
            logger.error("Not found web element by:"+by.toString());
        }
        return null;
    }


}
