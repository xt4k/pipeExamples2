package app.pageobject.menu;

import app.pageobject.BasePageObject;
import app.pageobject.form.SignInForm;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Selenide.$;

public class TopLinksMenuWidget extends BasePageObject {

    private SelenideElement seSignIn =$(By.xpath(".//*[@id='toplinks']//a[contains(@class,'btn')]"));

    @Step("Click Sign In")
    public SignInForm signIn() {
        seSignIn.click();
        attachPageScreenShot(getMethodName());//infoShot
        return new SignInForm();
    }
}
