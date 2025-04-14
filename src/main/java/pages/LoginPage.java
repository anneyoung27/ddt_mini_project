package pages;

import keywords.WebUI;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends WebUI {
    WebDriver driver;

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    By loginPage = By.xpath("//a[normalize-space()='SIGN-ON']");
    By userName = By.xpath("//input[@name='userName']");
    By password = By.xpath("//input[@name='password']");

    By submitButton = By.xpath("//input[@name='submit']");


    public void openLoginPage(){
        clickElement(loginPage);
    }

    public void setUserName(String u_name){
        setText(userName, u_name);
    }

    public void setPassword(String pwd){
        setText(password, pwd);
    }

    public void clickSubmitButton(){
        clickElement(submitButton);
    }

    public String getPageCurrentURL(){
        return "https://demo.guru99.com/test/newtours/login_sucess.php";
    }
}
