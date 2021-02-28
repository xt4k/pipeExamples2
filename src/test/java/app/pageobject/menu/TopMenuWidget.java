package app.pageobject.menu;

import app.pageobject.BasePageObject;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.fail;

public class TopMenuWidget extends BasePageObject {

    private SelenideElement seMore = $(By.xpath(".//*[@id='root']//div[3]/button"));

    @Step("Select Top menu item:`{0}`.")
    public void selectMenuItem(String menuItem) {
        switch (menuItem) {
            case "More":
                seMore.click();
                break;
            case "Help":
                break;
            default:
                fail("Not defined menuItem");
        }
        infoShot(getMethodName());
    }

}
