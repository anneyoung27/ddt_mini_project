package keywords;

import factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WebUI extends DriverFactory {

    public static Logger log = LogManager.getLogger(WebUI.class);

    public static WebElement getWebElement(By by){
        return driver.findElement(by);
    }

    public static List<WebElement> getWebElements(By by){
        return driver.findElements(by);
    }

    public static void openURL(String url){
        driver.get(url);
        double STEP_TIME = 0;
        threadSleep(STEP_TIME);
        log.info("Open URL: {}", url);
    }

    public static boolean checkElementExist(By by){
        List<WebElement> listElement = getWebElements(by);

        if (!listElement.isEmpty()){
            log.info("checkElementExists: " + true + " --- " + by);
            return true;
        }else {
            log.info("checkElementExists: " + false + " --- " + by);
            return false;
        }
    }

    public static void clickElement(By by){
        waitForElementVisible(by);
        getWebElement(by).click();
        log.info("Click element: {}", by.toString());
    }

    public static void setText(By by, String value){
        waitForElementVisible(by);
        getWebElement(by).sendKeys(value);
        log.info("Set text: {} on element {}", value, by);
    }

    public static String getElementText(By by){
        waitForElementVisible(by);
        String text = getWebElement(by).getText();
        log.info("Get text: {}", text);
        return text;
    }

    public static boolean isDisplayed(By by){
        boolean checkDisplay = driver.findElement(by).isDisplayed();
        log.info("Check element display {} \n=========> {}", by, checkDisplay);
        return checkDisplay;
    }

    // SYNCHRONIZATION
    public static void waitForElementVisible(By by){
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(System.getProperty("EXPLICIT_WAIT"))));
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            log.warn("Timeout waiting for the element visible. {}", by.toString());
        }
    }

    public static void waitForPageLoaded() {
        int waitTimeInSeconds = 30; // default fallback value
        String waitProperty = System.getProperty("EXPLICIT_WAIT");

        if (waitProperty != null) {
            try {
                waitTimeInSeconds = Integer.parseInt(waitProperty);
            } catch (NumberFormatException e) {
                log.warn("Invalid EXPLICIT_WAIT value. Using default: " + waitTimeInSeconds + " seconds");
            }
        } else {
            log.warn("EXPLICIT_WAIT system property is not set. Using default: " + waitTimeInSeconds + " seconds");
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTimeInSeconds));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        ExpectedCondition<Boolean> jsLoad = webDriver ->
                js.executeScript("return document.readyState").toString().equals("complete");

        try {
            if (!jsLoad.apply(driver)) {
                log.info("Waiting for JavaScript to be ready...");
                wait.until(jsLoad);
            }
        } catch (Exception e) {
            log.warn("FAILED. Timeout waiting for page load", e);
        }
    }

    public static void waitForElementPresent(By by){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(System.getProperty("TIMEOUT"))));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
        }catch (Throwable error){
            log.warn("Element not exists : {}", by.toString());
        }
    }

    public static void waitForElementToBeClicked(By by){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(System.getProperty("TIMEOUT"))));
            wait.until(ExpectedConditions.elementToBeClickable(by));
        }catch (Throwable error){
            log.warn("Timeout waiting for the element ready to click: {}", by.toString());
        }
    }

    public static void threadSleep(double second){
        try{
            Thread.sleep((long) (1000 * second));
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public static void scrollToElement(By element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", getWebElement(element));
    }

    public static void closeAlert(){
        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
}
