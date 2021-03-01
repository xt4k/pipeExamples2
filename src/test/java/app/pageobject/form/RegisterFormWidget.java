package app.pageobject.form;

import app.pageobject.BasePageObject;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.format;

public class RegisterFormWidget extends BasePageObject {

    private String xPathDraft = ".//*[@id='register-element-form']//input[@value='%s']";//id+"//input[@value='%s']";
    private String xPathSchool = ".//div[@class='school-item' and @title='%s']";
    private SelenideElement se;
    private SelenideElement seRegister = $(By.id("register-button"));
    private SelenideElement seSchool = $(By.id("school-typeahead"));
    private SelenideElement seRegisterForm = $(By.id("register-element-form"));
    private SelenideElement seSchoolList = $(By.xpath("//*[@id='register-element-form']//div[@class='school-select']"));


    @Step("Set Teacher registration form field `{0}`.")
    public RegisterFormWidget setField(String attributeValue, String text) {
        se = $(By.xpath(format(xPathDraft, attributeValue)));
        se.setValue(text);
        infoShot("Set " + attributeValue + " to " + text);
        return new RegisterFormWidget();
    }

    @Step("Select school.")
    public RegisterFormWidget selectSchool(String school) {
        seSchool.setValue(school);
       // seSchoolList.shouldBe(exist).shouldBe(visible);
        se = seRegisterForm.$x(format(xPathSchool, school));
        se.click();
        attachPageScreenShot(getMethodName());
        return new RegisterFormWidget();
    }

    @Step("Set school for failed cases.")
    public RegisterFormWidget setSchool(String school) {
        seSchool.setValue(school);
        attachPageScreenShot(getMethodName());
        return new RegisterFormWidget();
    }

    @Step("Do Register.")
    public void register() {
        seRegister.click();
        waitForLoaded();
        attachPageScreenShot(getMethodName());
    }
}
