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
    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Properties setUp = loadFile();
    public static WebDriverWait wait;

    public static String browser;

    public static Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory(){
        // to prevent external instantiation of this class
    }

    public static WebDriver getDriver(){
        return driver.get();
    }

    /**
     * driverSetup() method is called to set up driver
     */
    public static void driverSetUp() {
        if (getDriver() == null) {
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

                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(setUp.getProperty("IMPLICIT_WAIT"))));
                getDriver().manage().window().maximize();
                getDriver().get(setUp.getProperty("URL"));

                wait = new WebDriverWait(getDriver(), Duration.ofSeconds(Integer.parseInt(setUp.getProperty("EXPLICIT_WAIT"))));
            } catch (RuntimeException e) {
                log.error("Error during driver setup: ", e);
                throw new RuntimeException("Driver setup failed!", e);
            }
        }
    }

    /**
     * initializeDriver() method is called to set up the browser driver
     */
    private static void initializeDriver() {
        try{
            switch (setUp.getProperty("BROWSER").toLowerCase()) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver.set(new FirefoxDriver());
                    break;
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driver.set(new ChromeDriver());
                    break;
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless=new");
                    driver.set(new ChromeDriver(chromeOptions));
                    break;
                case "firefox-headless":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("-headless");
                    driver.set(new FirefoxDriver(firefoxOptions));
                    break;
                case "edge":
                    if (setUp.getProperty("PLATFORM_NAME").toLowerCase().contains("mac")){
                        log.warn("Your operating system does not support the requested browser: {}", setUp.getProperty("BROWSER"));
                        throw new WebDriverException("Your operating system does not support the requested browser!");
                    } else {
                        WebDriverManager.edgedriver().setup();
                        driver.set(new EdgeDriver());
                    }
                    break;
                case "safari":
                    if (setUp.getProperty("PLATFORM_NAME").toLowerCase().contains("windows")) {
                        log.warn("Your operating system does not support the requested browser: {}", setUp.getProperty("BROWSER"));
                        throw new WebDriverException("Your operating system does not support the requested browser!");
                    } else {
                        WebDriverManager.safaridriver().setup();
                        driver.set(new SafariDriver());
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


    /**
     * tearDown() method is called after every test. It closes the browser
     */
    public static void tearDown(){
        if (getDriver() != null){
            //getDriver().quit();
            getDriver().manage().deleteAllCookies();
            getDriver().close();
        }
        log.info("Driver closed");
    }

    /**
     * terminate() method is called after every class. It removes the ThreadLocal driver.
     */
    public static void terminate(){
        driver.remove();
    }

}
