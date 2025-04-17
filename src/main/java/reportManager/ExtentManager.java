package reportManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import factory.DriverFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ExtentManager {
    private static ExtentReports extentReports;

/**
 * getExtentReports() is a static method that creates and configures ExtenetReports object.
 * This method sets the theme of the report to STANDARD and title to "Test Report".
 */
public synchronized static ExtentReports getExtentReports() {
    if (extentReports == null) {
        String date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        ExtentSparkReporter htmlreporter = new ExtentSparkReporter( DriverFactory.setUp.getProperty("REPORT_PATH") + date);
        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlreporter);
        htmlreporter.config(
                ExtentSparkReporterConfig.builder().theme(Theme.STANDARD).documentTitle("Test Report").build());
    }
    return extentReports;
}

 }
