package testCases;

import factory.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import testHook.BaseTest;

public class LoginTest extends BaseTest {

    @Test
    public void testLoginValidInput(){
        LoginPage loginPage_1 = new LoginPage(DriverFactory.driver);

        loginPage_1.openLoginPage();

        loginPage_1.setUserName("selenium@qa");
        loginPage_1.setPassword("qa@12345");
        loginPage_1.clickSubmitButton();

        Assert.assertEquals(loginPage_1.getPageCurrentURL(), "https://demo.guru99.com/test/newtours/login_sucess.php");
    }

    @Test
    public void testLoginInvalidInput(){
        LoginPage loginPage_2 = new LoginPage(DriverFactory.driver);

        loginPage_2.openLoginPage();

        loginPage_2.setUserName("selenium");
        loginPage_2.setPassword("qa@123");
        loginPage_2.clickSubmitButton();

        Assert.assertEquals(loginPage_2.getPageCurrentURL(), "https://demo.guru99.com/test/newtours/login_sucess.php");
    }
}
