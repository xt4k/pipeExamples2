package app.pageobject;


import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static app.helper.BaseTestGui.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static java.lang.Boolean.valueOf;

public class BasePageObject {

    protected SelenideElement seEmail = $(By.ByXPath.id("UserUsername"));
    protected SelenideElement sePassword = $(By.ByXPath.id("UserPassword"));
    private  SelenideElement seErrorMsg=$(By.xpath(".//div[@class='bt-content']"));


       // public static final Logger LOG = LogManager.getLogger(BasePageObject.class);


    protected SelenideElement se;

    @Step("`{0}`.")
    public static void reportInfo(String sMessage) {
       // step(sMessage);
        LOG.info(sMessage);
    }

    @Step("`{0}`-page screenshot.")
    @Attachment(value = "`{0}` screenshot.", type = "image/png")
    public static byte[] attachPageScreenShot(String sShotName) {
        LOG.info(String.format("`%s`-page screenshot attached to report.", sShotName));
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Step("Wait for page refreshed (APP-side). [->] (atomic).")
    public void waitForLoaded() {//  String jsComplete = "return document.readyState == 'complete'";
        String jsComplete = "return document.readyState == 'complete'";
        ExpectedCondition<Boolean> pageLoad2 = Selenide -> valueOf(Objects.requireNonNull(executeJavaScript(jsComplete)).toString());
        Wait().until(pageLoad2);

    }





    @BeforeMethod
    public void logTestStart(Method method, Object[] params) {
        LOG.info("Start page_object {} with parameters {}", method.getName(), Arrays.toString(params));
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @AfterMethod(alwaysRun = true)
    public void logTestStop(Method method) {
        LOG.info("Stopped page_object {}", method.getName());
    }

    @Step("clear&set secret value to SelenideElement.")
    protected void setTextToField(SelenideElement seTextField, String sText, boolean bIsSecret) {
        seTextField.clear();
        seTextField.sendKeys(sText);
        if (bIsSecret) sText = "********";
        reportInfo(String.format("`%s` method seElement: `%s`, set to:`%s`.", getMethodName(), seTextField, sText));
    }

    @Step("Set text to Grid cell.")
    protected void setTextToGridCell(SelenideElement se, String text) {
        infoShot(getMethodName()+1);
        se.doubleClick()
                .$x("input")
                .sendKeys(text+ Keys.ENTER);
        infoShot(getMethodName()+9);
    }

    @Step("Check Error message.")
    public void checkError(String errorMsg) {
        seErrorMsg.shouldBe(exist)
                .shouldBe(visible)
                .shouldHave(text(errorMsg));
        infoShot(getMethodName());
    }

}
