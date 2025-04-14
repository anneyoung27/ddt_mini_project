package factory;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Properties;

import static helper.PropertiesHelper.loadFile;

public class DriverFactory {
    public static WebDriver driver;
    public static Properties setUp = loadFile();
    public static WebDriverWait wait;

    public static String browser;

    public static Logger log = LogManager.getLogger(DriverFactory.class);

    public static void driverSetUp() {
        if (driver == null) {
            try {
                if (setUp.isEmpty()) {
                    log.warn("Failed to load config.properties, using empty properties");
                } else {
                    log.info("config.properties file has been loaded successfully");
                }

                browser = System.getenv("BROWSER") != null && !System.getenv("BROWSER").isEmpty()
                        ? System.getenv("BROWSER")
                        : setUp.getProperty("BROWSER");

                setUp.setProperty("BROWSER", browser);
                initializeDriver();

                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(setUp.getProperty("IMPLICIT_WAIT"))));
                driver.manage().window().maximize();
                driver.get(setUp.getProperty("URL"));

                wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(setUp.getProperty("EXPLICIT_WAIT"))));
            } catch (RuntimeException e) {
                log.error("Error during driver setup: ", e);
                throw new RuntimeException("Driver setup failed!", e);
            }
        }
    }

    private static void initializeDriver() {
        try{
            switch (setUp.getProperty("BROWSER").toLowerCase()) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(new ChromeOptions().addArguments("--headless=new"));
                    break;
                case "firefox-headless":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(new FirefoxOptions().addArguments("-headless"));
                    break;
                case "edge":
                    if (setUp.getProperty("PLATFORM_NAME").toLowerCase().contains("mac")){
                        log.warn("Your operating system does not support the requested browser: {}", setUp.getProperty("BROWSER"));
                        throw new WebDriverException("Your operating system does not support the requested browser!");
                    } else {
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver();
                    }
                    break;
                case "safari":
                    if (setUp.getProperty("PLATFORM_NAME").toLowerCase().contains("windows")) {
                        log.warn("Your operating system does not support the requested browser: {}", setUp.getProperty("BROWSER"));
                        throw new WebDriverException("Your operating system does not support the requested browser!");
                    } else {
                        WebDriverManager.safaridriver().setup();
                        driver = new SafariDriver();
                    }
                    break;
                default:
                    throw new RuntimeException("Invalid browser specified in configuration: " + setUp.getProperty("BROWSER"));
            }
        }catch (UnreachableBrowserException e) {
            log.error(e);
        }
        log.info("{} browser has been selected", setUp.getProperty("BROWSER"));
    }


    public static void tearDown(){
        if (driver != null){
            driver.quit();
            driver = null;
        }
        log.info("Driver closed");
    }

}
