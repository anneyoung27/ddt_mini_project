package testHook;

import factory.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void init(){
        DriverFactory.driverSetUp();
    }

    @AfterMethod
    public void quit(){
        DriverFactory.tearDown();
    }
}
