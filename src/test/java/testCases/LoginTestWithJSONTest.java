package testCases;

import factory.DriverFactory;
import helper.JsonHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import testHook.BaseTest;

public class LoginTestWithJSONTest extends BaseTest {

    @Test(dataProvider = "userData")
    public void testLoginWithJson(String u_name, String pwd){
        LoginPage loginPage_1 = new LoginPage(DriverFactory.driver);

        loginPage_1.openLoginPage();

        loginPage_1.setUserName(u_name);
        loginPage_1.setPassword(pwd);
        loginPage_1.clickSubmitButton();

        Assert.assertEquals(loginPage_1.getPageCurrentURL(), "https://demo.guru99.com/test/newtours/login_sucess.php");
    }

    @DataProvider
    public Object[][] userData() throws Exception{
        JsonHelper jsonHelper = new JsonHelper();
        JSONArray usersList = jsonHelper.readJson(System.getProperty("user.dir") + "/src/test/java/resources/login.json", "users");
        Object [][] dataObj = new Object[usersList.size()][2];

        for (int i = 0; i <  dataObj.length; i++){
            JSONObject user = (JSONObject) usersList.get(i);
            dataObj[i][0] = user.get("username");
            dataObj[i][1] = user.get("password");
        }
        return dataObj;
    }
}
