package utilities;

import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.Logger;
import reportManager.Report;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListeners implements ITestListener {

    static Logger log = LogManager.getLogger(Report.class);

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n" + "  ***** Test Executing : " + result.getName());
        log.info("  ***** Test Executing : " + result.getName());
        Report.startTest(result.getMethod().getMethodName(), result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test Passed : " + result.getName());
        Report.log(Status.PASS, " Test Passed", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
       log.info("Test Failed : " + result.getName());


    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }
}
