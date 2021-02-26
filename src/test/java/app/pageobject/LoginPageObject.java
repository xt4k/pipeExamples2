package app.pageobject;

import app.pageobject.MainPageObject;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;


public class LoginPageObject extends BasePageObject {


    private SelenideElement seUserPassword = $(By.id("user-psw"));
    private SelenideElement seAppVersion = $(By.xpath(". //*[@class='app-version']"));

    private SelenideElement seLabelErrorMessage = $(By.xpath(".//div[@class='error']"));// $(By.xpath(".//section/div[contains(@class,'error')]"))
    private SelenideElement seUserName = $(By.id("user-name"));
    private SelenideElement seBtnLogin = $(By.id("btn-submit"));

    @Step("Login to APP.")
    public MainPageObject login(HashMap<String, String> hmLogin) {
        logAPPInfo(seAppVersion);
        // reportInfo("loginMap data: " + hmLogin);
        // String userAgent = (String) ((JavascriptExecutor) SelenideDriver).executeScript("return navigator.userAgent;");
        // LOG.info(String.format("userAgent: %s",userAgent));     //  System.out.println("useragent!: "+userAgent);
        setTextToField(seUserName, hmLogin.get("txt_Pin"), false);      //  setPin(mLogin.get("txt_Pin")); //  setPassword(mLogin.get("txt_Password"));
        setTextToField(seUserPassword, hmLogin.get("txt_Password"), true);
        sCrewMemberPin = hmLogin.get("txt_Pin");
        step("LOGIN CREW MEMBER PIN:" + sCrewMemberPin);
        seBtnLogin.click();
        waitForLoaded();
        return new MainPageObject();
    }

    @Step("Step to check error message with Negative login test cases with data: {0}")
    public SelenideElement tryLogin(Map negativeLoginData) {
        setTextToField(seUserName, negativeLoginData.get("txt_Pin").toString());      //  setPin(login.get("txt_Pin")); //  setPassword(login.get("txt_Password"));
        setTextToField(seUserPassword, negativeLoginData.get("txt_Password").toString());
      /*  setPin( negativeLoginData.get("txt_Pin").toString());
        setPassword(negativeLoginData.get("txt_Password").toString());*/
        seBtnLogin.click();
        seBtnLogin.click();
        return seLabelErrorMessage;
    }

    @Step("Step to check if 'Login' button is enabled. (For negative Login scenario testcases): {0}}")
    public Boolean btnLoginIsEnabled() {
        return !Boolean.parseBoolean(seBtnLogin.getAttribute("disabled"));
    }

}