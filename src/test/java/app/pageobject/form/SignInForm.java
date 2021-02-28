package app.pageobject.form;

import app.pageobject.BasePageObject;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SignInForm extends BasePageObject {
    private ElementsCollection ecTabs = $$(By.xpath(".//*[@class='header-menu']//div[@class]"));
    private SelenideElement seSignIn =$(By.id("signin-btn"));
    private  SelenideElement seToMainPage = $(By.xpath(".//div[@class='header-left']"));


@Step("Sign In as a `{0}`")
public SignInForm as(String tabName) {
    se = ecTabs.findBy(text(tabName));
    se.click();
    infoShot(getMethodName());
    return new SignInForm();
    }

    @Step("Set School email:`{0}`")
    public SignInForm setEmail(String email) {
        seEmail.setValue(email);
        infoShot(getMethodName());
        return new SignInForm();
    }

    @Step("Set Password:`{0}`")
    public SignInForm setPass(String pass) {
        sePassword.setValue(pass);
        infoShot(getMethodName());
        return new SignInForm();
}
    @Step("Sign In")
    public void signOn() {
        seSignIn.click();
        infoShot(getMethodName());
    }

    @Step("Go to Home page.")
    public void toMainPage() {
    seToMainPage.click();
        infoShot(getMethodName());
}

}
