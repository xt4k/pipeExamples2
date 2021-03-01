package app.pageobject.menu;

import app.pageobject.BasePageObject;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.fail;

public class HeaderLinksWidget extends BasePageObject {
    private SelenideElement seMore = $(By.xpath(".//a[@class='header-more-menu-icon']"));

    @Step("Select Header Link `{0}` in My Account")
    public void selectLink(String linkItem) {
        switch (linkItem) {
            case "...":
                seMore.click();
                break;
            case "Help":
                break;
            default:
                fail("Not defined linkItem");
        }
        attachPageScreenShot(getMethodName());
    }
}

