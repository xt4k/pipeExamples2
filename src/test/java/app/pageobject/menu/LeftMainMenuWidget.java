package app.pageobject.menu;

import app.pageobject.BasePageObject;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Selenide.$$;

public class LeftMainMenuWidget extends BasePageObject {
    private ElementsCollection ecMenuLinks = $$(By.xpath("//*[@id='root']//div[contains(@style,'margin-left')]//a[@href]"));

    @Step("Select Left Menu item:`{0}`.")
    public void selectMenuItem(String link) {
        se = ecMenuLinks.findBy(Condition.text(link));
        se.click();
        attachPageScreenShot(getMethodName());
    }
}
