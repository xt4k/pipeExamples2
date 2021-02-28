package app.pageobject;

import app.pageobject.BasePageObject;
import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class ConfirmationPopUpWidget extends BasePageObject {
    private ElementsCollection ecButtons = $$(By.xpath(".//button[@class='typography--buttonFlat___3HVSj']"));

    @Step("Select `{0}` on confirmation popup.")
    public void select(String select) {
        ecButtons.findBy(text(select)).click();
        infoShot(getMethodName());
    }
}
