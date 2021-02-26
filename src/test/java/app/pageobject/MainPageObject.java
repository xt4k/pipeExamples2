package app.pageobject;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;

public class MainPageObject extends BasePageObject {

    private SelenideElement seLinkMasterCash = $(By.xpath(".//*[@id='bcMasterCashTile']/a"));
    private SelenideElement seLinkCabinAllocation = $(By.xpath(".//*[@id='bcCabinAllocationTile']/a"));
    private SelenideElement seLinkMyPersonalDetails = $(By.xpath(".//*[@id='bcTilePersonalDetails']/a"));


    @Step("Navigate to `{0}` module via top links-row.")
    public void goLink(String sModuleName) {
        switch (sModuleName) {
            case "Master Cash":
                seLinkMasterCash.click();
                break;
            case "Cabin Allocation":
                seLinkCabinAllocation.click();
                break;
            case "My Personal Details":
                seLinkMyPersonalDetails.click();
                break;
            default:
                refresh();
                break;
        }
        locally_updated();
        waitForLoaded();
    }


}
