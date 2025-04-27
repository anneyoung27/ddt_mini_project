package testCases;

import com.codoid.products.exception.FilloException;
import factory.DriverFactory;
import helper.ExcelHelper;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import testHook.BaseTest;

import java.util.HashMap;
import java.util.List;

public class LoginTestWithExcelTest extends BaseTest {
    @Test(dataProvider = "userData")
    public void testExcelLogin(String u_name, String pwd){
        LoginPage loginPage_1 = new LoginPage(DriverFactory.getDriver());

        loginPage_1.openLoginPage();

        loginPage_1.setUserName(u_name);
        loginPage_1.setPassword(pwd);
        loginPage_1.clickSubmitButton();

        Assert.assertEquals(loginPage_1.getPageCurrentURL(), "https://demo.guru99.com/test/newtours/login_sucess.php");
    }

    @DataProvider
    public Object[][] userData() throws FilloException {
        ExcelHelper excelHelper = new ExcelHelper();
        List<HashMap<String, String>> users = excelHelper.getAllData(System.getProperty("user.dir") + "\\src\\test\\java\\resources\\",
                "testdata.xlsx",
                "TestData");
        Object[][] dataObj = new Object[users.size()][2];

        for (int i = 0; i < users.size(); i++){
            dataObj[i][0] = users.get(i).get("Username");
            dataObj[i][1] = users.get(i).get("Password");
        }
        return dataObj;
    }
}
