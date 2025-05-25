package utilities;

import com.aventstack.extentreports.Status;
import factory.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.logging.log4j.Logger;
import reportManager.Report;

import java.io.File;
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

       try{
           String date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());

           // Object currentInstance = result.getInstance();
           TakesScreenshot ts = (TakesScreenshot) (DriverFactory.getDriver());

           File source = ts.getScreenshotAs(OutputType.FILE);
           String fileDestination = System.getProperty("user.dir") + "/screenshots/" + result.getName() + "_" + date + ".png";
           File finalDestination = new File(fileDestination);

           FileUtils.copyFile(source, finalDestination);

           Report.log(Status.FAIL, " Test Failed " + result.getThrowable(), result.getName());
           Report.getTest().addScreenCaptureFromPath(fileDestination);
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("Test Skipped: " + result.getName());
        Report.log(Status.SKIP, " Test Skipped " + result.getThrowable(), result.getName());
    }


    @Override
    public void onFinish(ITestContext context) {
        Report.endTest();
        System.out.println("\n" + "---------------- TEST EXECUTION FINISHED ---------------- ");
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        Report.endTest();
        System.out.println("\n" + "---------------- TEST EXECUTION FAILED TIMEOUT ---------------- ");
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n" + "---------------- TEST EXECUTION STARTED ---------------- ");
    }
}
