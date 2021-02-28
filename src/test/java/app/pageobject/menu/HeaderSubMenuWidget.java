package app.pageobject.menu;

import app.pageobject.BasePageObject;
import app.pageobject.form.SignInForm;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class HeaderSubMenuWidget extends BasePageObject {
    private ElementsCollection ecLinkItems =$$(By.xpath(".//div[@class='header-more-menu']//a"));

    @Step("Select link item `{0}`")
    public SignInForm selectLink(String linkItem) {
        SelenideElement se = ecLinkItems.findBy(text(linkItem));
        se.click();
        infoShot(getMethodName());
        return new SignInForm();
    }
}
