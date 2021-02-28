package app.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static java.lang.String.format;
import static java.lang.System.getProperty;

public class PopupInfoWidget extends BasePageObject {
    SelenideElement seLabel =$(By.xpath(".//div[@class='typography--titleLarge___1atmR']"));
    SelenideElement seTitle =$(By.xpath(".//div[@class='typography--dialogTitleWhite___1HzeK']"));

    @Step("Check popup text.")
    public PopupInfoWidget verifyText() {
        seTitle.shouldHave(text(getProperty("student.registered.done")));
        seLabel.shouldHave(text(format(getProperty("student.registered"),1)));
        return new PopupInfoWidget();
    }

    @Step("Close popup.")
    public void closeIt()
    {
        seTitle.preceding(0).lastChild().click();
    }

}
