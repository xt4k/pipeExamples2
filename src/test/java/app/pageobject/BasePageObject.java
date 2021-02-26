package app.pageobject;



import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedCondition;
import java.util.Objects;
import static app.helper.BaseTestGui.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static java.lang.Boolean.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

public class BasePageObject {
    static String sCrewMemberPin;
    private static SelenideElement seLoadPanelContainer = $(By.id("loadPanelContainer"));
    private static SelenideElement seButtonContainer = $(By.id("buttonContainer"));
       // public static final Logger LOG = LogManager.getLogger(BasePageObject.class);

    protected String sExpectedEmptyCode = "&nbsp;";
    protected SelenideElement sePageLabel = $(By.xpath(".//h3/b"));
    protected SelenideElement seQuickNavMenu = $(By.id("divQuickNav"));
    private SelenideElement seLabelWheelLoading = $(By.xpath("//*[@class='dx-loadpanel-message']"));//id("pcLoadingScreen_PW-1"));//pcLoadingScreen_PWC-1"));
    private SelenideElement seLoadPanel = $(By.id("loadPanel"));
    //private SelenideElement btn_LogOff = $("#logOffLink");
    private SelenideElement seBtnLogOff = $(By.id("logOffLink"));
    private SelenideElement seRelieverPicto = $(By.id("reliever-button-off"));//pcLoadingScreen_PWC-1"));
    private SelenideElement seGoParentMenu = $(By.id("divPageLabel"));
    private SelenideElement seVesselabel = $(By.xpath(".//*[@class='header-left']"));
    private SelenideElement sePopup = $(By.id("popup"));
    private SelenideElement seAnyPopup = $(By.xpath(".//*[contains(@id,'popup-')]"));
    private SelenideElement seEmptyGridSearchResultLabelText = $(By.xpath(".//*[@class='dx-datagrid-nodata']"));
    private SelenideElement seBreadCrumbActive = $(By.xpath(".//li[@class='active']"));
    // private SelenideElement seLinkBack = $(By.xpath(".//a/img[@src]"));
    private SelenideElement seCircledArrow = $(By.id("bottle"));
    private SelenideElement seCrewPin = $(By.id("someId"));
    private SelenideElement seBtnPopUpCancel = $(By.id("cancel"));
    //private SelenideElement seBtnPopUpCloseX = sePopup.$x(".//div[@aria-label='close']");
    private String sBtnPopUpCloseX = ".//div[@aria-label='close']";

    private SelenideElement seWarningDialogWindow = $(By.id("pcWarningDialog_PW-1"));
    private SelenideElement seWarningDialogWindowClose = $(By.id("pcWarningDialog_HCB-1"));


    // <div id="loadPanelContainer"></div>
    //<div id="buttonContainer"></div>
    private SelenideElement seLoginForm = $(By.id("login-form"));

    @Step("`{0}`.")
    public static void reportInfo(String sMessage) {
       // step(sMessage);
        LOG.info(sMessage);
    }

    @Step("`{0}`-page screenshot.")
    @Attachment(value = "`{0}` screenshot.", type = "image/png")
    public static byte[] attachPageScreenShot(String sShotName) {
        LOG.info(String.format("`%s`-page screenshot attached to report.", sShotName));
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Step("Wait for page refreshed (APP-side). [->] (atomic).")
    public void waitForLoaded() {//  String sDocLoaded = "return document.readyState == 'complete'";
        String sDocLoaded = "return (document.readyState == 'complete' && jQuery.active == 0)";
        ExpectedCondition<Boolean> pageLoad2 = Selenide -> valueOf(Objects.requireNonNull(executeJavaScript(sDocLoaded)).toString());
        // String sLogMsg = String.format("`%s` use JS:`%s` which return:`%s`", getMethodName(), sDocLoaded, pageLoad2);       // reportInfo(sLogMsg);
        Wait().until(pageLoad2);        // LOG.info("'=>' is disappeared");
        seLoadPanelContainer.shouldNotBe(visible);
        seButtonContainer.shouldNotBe(visible);
        seLabelWheelLoading.shouldNotBe(visible);
        seLoadPanel.shouldNotBe(visible);
       // step("Wait for page refreshed (APP server side). ([=>]).");
        // seLoadPanel.shouldNotHave(cl())
    }

    @Step("Wait while internal APP refreshed locally.")
    public BasePageObject locally_updated() {  // Selenide.sleep(1000);//label_wheelLoading.shouldBe(visible);
        // <div id="loadPanelContainer"></div>
        //<div id="buttonContainer"></div>
       // step("Wait while internal APP refreshed locally.");
        seLabelWheelLoading.shouldNotBe(visible);        //return this;
        LOG.info("{} , * was gone ", this.getClass());
        return new BasePageObject();
    }

    @Step("Get APP version and Pure tests run duration (initial piont right after login to APP).")
    void logAPPInfo(SelenideElement se) {
        reportInfo(String.format("Server: '%s', APP: '%s'.", baseUrl, se.getText()));
        String[] appVersion = se.getText().split("\\s+");
        System.setProperty(("app.version"), appVersion[1]);
    }

    @Step("Dom element {0} as expected HAS NO text:`{1}`, so: `{2}`.")
    protected void validateElementHasNoSuchText(SelenideElement se, String sValidationText, String sMessage) {
        se.shouldNotHave(text(sValidationText));
        String sLogValidationMessage = String.format("`%s` se:`%s` HAS NO text:`%s`", sMessage, se, sValidationText);
        reportInfo(sLogValidationMessage);
    }

    @Step("Dom element {0} as expected HAS text:`{1}`, so: `{2}`.")
    protected void validateElementText(SelenideElement se, String sValidationText, String sMessage) {
        se.shouldHave(text(sValidationText));
        String sLogValidationMessage = String.format("`%s` se:`%s` has text:`%s`", sMessage, se, sValidationText);
        reportInfo(sLogValidationMessage);
    }


    @Step("clear&set value:`{1}` to SelenideElement.")
    protected void setTextToField(SelenideElement seTextField, String sText) {
        seTextField.clear();
        seTextField.sendKeys(sText);
        reportInfo(String.format("`%s` method seElement: `%s`, set to:`%s`.", getMethodName(), seTextField, sText));
    }

/*
    @Step("Navigate to TimeAndAttendance menu item")
    public TimeAndAttendancePageObject goMenu2TimeAndAttendance() {//go into Time and Attendance top-menu item
        waitForLoaded();
        new MainPageObject().goPictoTimeAndAttendance();
        return new TimeAndAttendancePageObject();
    }

    @Step("Navigate to 'My_TimeSheets' menu item")
    MyTimeSheetsPageObject goMenu3MyTimesheets() {//go into My Timesheets menu item
        waitForLoaded();
        return new TimeAndAttendancePageObject().goPictoMyTimeSheets();
    }

    @Step("Navigate to 'Co-Workers_TimeSheets' menu item")
    public CoWorkersTimeSheetsPageObject goMenu3CoWorkersTimesheets() {//goCoWorkersTimeSheetsPage()
        return new TimeAndAttendancePageObject().goCoWorkersTimeSheetsPage();
    }

    @BeforeMethod
    public void logTestStart(Method method, Object[] params) {
        LOG.info("Start page_object {} with parameters {}", method.getName(), Arrays.toString(params));
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @AfterMethod(alwaysRun = true)
    public void logTestStop(Method method) {
        LOG.info("Stopped page_object {}", method.getName());
    }*/

    @Step("clear&set secret value to SelenideElement.")
    protected void setTextToField(SelenideElement seTextField, String sText, boolean bIsSecret) {
        seTextField.clear();
        seTextField.sendKeys(sText);
        if (bIsSecret) sText = "********";
        reportInfo(String.format("`%s` method seElement: `%s`, set to:`%s`.", getMethodName(), seTextField, sText));
    }

    @Step("Returns Vessel name.")
    String sGetPageLabelVesselName(SelenideElement se) {
        String sVessel = seVesselabel.getText();
        infoShot(getMethodName());
        return sVessel;
    }

/*    @Step("Wait for page refreshed (APP-side). [->] (atomic).")
    public static void waitForLoaded() {
        String documentLoaded = "return (document.readyState == 'complete' && jQuery.active == 0)";
        ExpectedCondition<Boolean> pageLoad2 = Selenide -> Boolean.valueOf(com.codeborne.selenide.Selenide.executeJavaScript(documentLoaded).toString());
        Selenide.Wait().until(pageLoad2);        // LOG.info("'=>' is disappeared");
    }*/

    @Step("Search and select element with text {2} from elements collection in Control.")
    protected void searchStringCommon(SelenideElement se, ElementsCollection ec, String sSearch) {
        reportInfo(String.format("Click on element:`%s` with text:`%s`.", se, sSearch));
        se.sendKeys(sSearch);
        waitForLoaded();
        sleep(300);
        ec.find(text(sSearch)).click();
        waitForLoaded();
    }

    @Step("Return hint message from timeCard.")
    protected String sGetTimeCardHint(SelenideElement seHint) {
        String sHint;
        if (seHint.getAttribute("data-dxhint-title") == null) {
            sHint = seHint.getAttribute("title");
        } else {
            sHint = seHint.getAttribute("data-dxhint-title");
        }
        return sHint;
    }
    @Step("Set text:`{1}`.")
    protected void setTextToInputField(SelenideElement seInputField, String sText) {
        waitForLoaded();
        seInputField.hover().clear();
        //seInputField.sendKeys(Keys.HOME);
        seInputField.sendKeys(sText);
        seInputField.sendKeys(Keys.ENTER);
        locally_updated();
        waitForLoaded();
        reportInfo(String.format("Set text:`%s` to SelenideElement:`%s`.", sText, seInputField.getTagName()));
    }

    @Step("click SelenideElement by XPathId:{0} via JS code (atomic).")
    public void clickByJs(String sXpathId) {
        waitForLoaded();
        String sJsCode = "document.getElementById('" + sXpathId + "').click();";
        reportInfo(String.format("Try click SelenideElement by XPathId:'%s' via JS code:'%s'.", sXpathId, sJsCode));
        executeJavaScript(sJsCode);
    }

    @Step("click defined SelenideElement via JS code (atomic).")
    public void clickSeByJs2(SelenideElement se) {
        waitForLoaded();
        String sXpathId = se.getAttribute("id");
        String sJsCode = "document.getElementById('" + se.getAttribute("id") + "').click();";
        reportInfo(String.format("Try click SelenideElement (XPath):'%s' via JS code2:'%s'.", sXpathId, sJsCode));
        executeJavaScript(sJsCode);
    }

    @Step("click defined SelenideElement via JS code (atomic).2")
    public void clickSeByJs(SelenideElement se) {
        se.scrollIntoView(true);
        waitForLoaded();
        String sJsCode2 = "arguments[0].click()";
        reportInfo(String.format("Try click SelenideElement (XPath):'%s' via JS code1:'%s'.", se, sJsCode2));
        executeJavaScript(sJsCode2, se);
    }

    @Step("Set:`{2}` to SelenideElement's:`{0}` attribute:`{1}` by JS (atomic).")
    public void setSeAttributeByJs(SelenideElement se, String sAttribute, String sText) {
        String sJsCode = "arguments[0].setAttribute(arguments[1], arguments[2]);";
        //reportInfo(sJsCode);
        executeJavaScript(sJsCode, se, sAttribute, sText);
        String sLogMsg = String.format("`%s` set text:`%s` to attribute:`%s` by JS:`%s`.", getMethodName(), sText, sAttribute, sJsCode);
        locally_updated();
        waitForLoaded();
        reportInfo(sLogMsg);
        infoShot(sLogMsg);
    }

    @Step("Set:`{1}` to SelenideElement's:`{0}` by JS (atomic).")
    public void setSeAttributeTimeByJs(SelenideElement se, String sText) {
        String sId = se.getAttribute("id");
        // String sJsCode = String.format("var daytime = new Date('%s');arguments[0].setAttribute('value',daytime.getTime());",sText);
        String sJsCode = String.format("document.getElementById(\"%s\").value = \"%s\";", sId, sText);
        reportInfo(getMethodName() + ". sJsCode: " + sJsCode);
        executeJavaScript(sJsCode);
        String sLogMsg = String.format("`%s`set `value` to:`%s` by JS:`%s`.", getMethodName(), sText, sJsCode);
        locally_updated();
        waitForLoaded();
        reportInfo(sLogMsg);
        infoShot(sLogMsg);
    }

    @Step("Set:`{1}` to SelenideElement's:`{0}` by JS (atomic).")
    public void setSeAttributeAndOnChangeEventByJs(SelenideElement se, String sText) {
        String sId = se.getAttribute("id");
        // String sJsCode = String.format("var daytime = new Date('%s');arguments[0].setAttribute('value',daytime.getTime());",sText);
        //   String sJsCode = String.format("document.getElementById(\"%s\").value = \"%s\";",sId,sText);
        //"arguments[0].setAttribute(arguments[1], arguments[2]);"
        //String sJsCode3 = String.format("document.getElementById(\"%s\").value = \"%s\";",sId,sText);
        String sJsCode = String.format("var elementTimeInCalendar = document.getElementById(\"%s\");" +
                "elementTimeInCalendar.value = \"%s\";" +
                "var elEvent = new Event('onchange', {});" +
                "elementTimeInCalendar.dispatchEvent(elEvent);" +
                "elementTimeInCalendar.onchange = \"%s\";", sId, sText, sText);
        reportInfo(getMethodName() + " for se:" + sId + ", sJsCode: " + sJsCode);
        executeJavaScript(sJsCode);
        String sLogMsg = String.format("`%s`set `value` to:`%s` by JS:`%s`.", getMethodName(), sText, sJsCode);
        locally_updated();
        waitForLoaded();
        reportInfo(sLogMsg);
        infoShot(sLogMsg);
    }


    @Step("Set:`{2}` attribute:`{1}` in SelenideElement:`{0}` (by JS code (atomic)).")
    public void setToAttributeElementTextByXpathJs2(SelenideElement se, String sAttribute, String sText) {

        // String sJsCode = String.format("document.evaluate('%s').setAttribute('%s', '%s')", sXpathId, sAttribute, sText);
        //String sJsCode = String.format("document.evaluate('%s').setAttribute('%s', '%s')", sXpathId, sAttribute, sText);
        //String sJsCode = String.format("$('%s').attr('%s', '%s')", sXpathId, sAttribute, sText);
        String sJsCode = "arguments[0].setAttribute(arguments[1], arguments[2]);arguments[0].onchange();";//, se, sAttribute, sText);
        // String sJsCode = String.format("$(arguments[0]).%s = '%s')", sAttribute, sText);
        reportInfo(sJsCode);
        executeJavaScript(sJsCode, se, sAttribute, sText);
        String sLogMsg = String.format("`%s` set text:`%s` set to `%s` attribute by JS code:`%s`.", getMethodName(), sText, sAttribute, sJsCode);
        reportInfo(sLogMsg);
        infoShot(sLogMsg);
    }

    @Step("Set text to SelenideElement's attribute via JS code (atomic).")
    public void setToAttributeElementTextByXpathJs(String sXpathId, String sAttribute, String sText) {
        SelenideElement se = $(By.id(sXpathId));
        // String sJsCode = String.format("document.evaluate('%s').setAttribute('%s', '%s')", sXpathId, sAttribute, sText);
        //String sJsCode = String.format("document.evaluate('%s').setAttribute('%s', '%s')", sXpathId, sAttribute, sText);
        //String sJsCode = String.format("$('%s').attr('%s', '%s')", sXpathId, sAttribute, sText);
        String sJsCode = "arguments[0].setAttribute(arguments[1], arguments[2]);";//, se, sAttribute, sText);
        // String sJsCode = String.format("$(arguments[0]).%s = '%s')", sAttribute, sText);
        reportInfo(sJsCode);
        executeJavaScript(sJsCode, se, sAttribute, sText);
        String sLogMsg = String.format("`%s` set text:`%s` set to `%s` attribute by JS code:`%s`.", getMethodName(), sText, sAttribute, sJsCode);
        reportInfo(sLogMsg);
        infoShot(sLogMsg);
    }

    @Step("return some text via JS code (atomic).")
    public boolean getTextToElementByXpathJs(String stringXpath, String inputText) {
        String jsCode1 = "var text2 = document.evaluate(" + stringXpath + ", document, null, XPathResult.STRING_TYPE, null);";
        String jsCode2 = "var text = document.getElementById(" + stringXpath + ").innerText; return text;";
        String jsCode3 = "var text = document.getElementById(" + stringXpath + ").innerHTML; return text;";
        String jsCode4 = "var sometext = document.evaluate(" + stringXpath + ", document, null, XPathResult.STRING_TYPE, null); return sometext.stringValue";
        String jsCode5 = "var text11 =  document.evaluate(path, document, null, XPathResult.ANY_TYPE, null).singleNodeValue; return text11;";
        String jsCode6 = "var text = document.getElementById(" + stringXpath + ").innerHTML; return text;";
        String jsCode7 = "var sometext = document.evaluate(" + stringXpath + ", document, null, XPathResult.STRING_TYPE, null); return sometext.stringValue";
        String jsCode8 = "var text11 =  document.evaluate(path, document, null, XPathResult.ANY_TYPE, null).singleNodeValue; return text11;";
        String str = executeJavaScript(jsCode5);
        String common = String.format("here string from pure javascript `%s`.", str);
        reportInfo(common + jsCode1 + jsCode2 + jsCode3 + jsCode4 + jsCode5 + jsCode6 + jsCode7 + jsCode8);
        return true;
    }

    @Step("Log out from APP")
    public LoginPageObject logOff() {
        seBtnLogOff.click();
        infoShot(getMethodName());
        return new LoginPageObject();
    }

    @Step("Return True if logged in to APP")
    public boolean bIsLoggedIn() {
        boolean bIsLogIn = seBtnLogOff.exists();
        boolean bLoginForm = seLoginForm.exists();
        boolean bResult = bIsLogIn && (!bLoginForm);
        infoShot(getMethodName() + ": " + bResult);
        return bResult;
    }

    @Step("Return `True` if we are inside expected APP menu item (`{0}`).")
    public boolean isInsideExpectedMenuItem(String sMenuItemName) {
        infoShot("Should be in 'Time And Attendance' menu.");
        boolean bOnRightPage = false;
        String sMenuPlace = seCircledArrow.parent().parent().lastChild().getText();

        if (seCircledArrow.isDisplayed() && sMenuPlace.equals(sMenuItemName))
            bOnRightPage = true;
        LOG.info(String.format("Inside'Time And Attendance' menu:'%s'", bOnRightPage));
        return bOnRightPage;
    }


    @Step("Go Parent Menu.")
    public void goParentMenu() {
        seGoParentMenu.click();
        waitForLoaded();
        infoShot(getMethodName());
    }

    @Step("Go Previous Menu Back by `CircledArrow` button.")
    public void goBackMenuCircledArrow() {
        seCircledArrow.click();
        locally_updated();
        waitForLoaded();
        infoShot(getMethodName());
    }



    @Step("Return true if current page title equal to expected:`{0}`.")
    public boolean isOnPage(String sText) {
        boolean bOnPage = false;
        if (sePageLabel.getText().toUpperCase().equals(sText.toUpperCase())) bOnPage = true;
        infoShot(getMethodName());
        return bOnPage;
    }

    @Step("Check: Selenide Element should have text:`{1}` (or empty).")
    protected void seTextCheck3(String sSeXpath, String sValue) {
        SelenideElement se = $(By.xpath(sSeXpath));
        se.scrollIntoView(true);
        String sLogMsg = String.format("`Out of screen` SE - EXPECTED/ACTUAL `text: `%s/%s (`%s`- html_code).",
                sValue, se.innerText(), se.innerHtml());
        infoShot(sLogMsg);
        step(sLogMsg);
        if ((sValue == null) || (sValue.toUpperCase().equals("NULL")))
            assertEquals(se.innerHtml(), "&nbsp;", "Actual Value:`" + se.innerHtml() + "` not equal to Expected:`NULL`! ");
        else
            assertEquals(se.innerText().trim(), sValue, String.format("Actual Value:`%s` not equal to Expected:`%s`", se.innerText(), sValue));
    }

    @Step("Check: Selenide Element `{2}` should have text:`{1}` (or empty).")
    protected void seTextCheck(String sSeXpath, String sValue, String sAllureComment) {
        SelenideElement se = $(By.xpath(sSeXpath));
        se.scrollIntoView(true);
        String sLogMsg = String.format("`Out of screen` SE - EXPECTED/ACTUAL `text: `%s/%s (`%s`- html_code).",
                sValue, se.innerText(), se.innerHtml());
        infoShot(sLogMsg);
        step(sLogMsg);
        if ((sValue == null) || (sValue.toUpperCase().equals("NULL")))
            assertEquals(se.innerHtml(), "&nbsp;", "Actual Value:`" + se.innerHtml() + "` not equal to Expected:`NULL`! ");
        else
            assertEquals(se.innerText().trim(), sValue, String.format("Actual Value:`%s` not equal to Expected:`%s`", se.innerText(), sValue));
    }


    @Step("Check that SE:`{0}` attribute:`{1}` has state:`{2}`.")
    protected void checkSeButtonState(String seId, String sAttribute, boolean bState) {
        SelenideElement se = $(By.id(seId));
        String sAttributeState = String.valueOf(bState);
        se.shouldHave(attribute(sAttribute, sAttributeState));
    }

    @Step("Check that page contains SE with id:`popup`.")
    public boolean isPopUp() {
        boolean bIsPopUp = sePopup.exists();
        reportInfo("PopUp Se is exist: " + bIsPopUp);
        return bIsPopUp;
    }

    @Step("Check if on page present some popup.")
    public boolean isAnyPopUp() {
        boolean bIsPopUp = seAnyPopup.exists();
        reportInfo("some PopUp Se is exist: " + bIsPopUp);
        return bIsPopUp;
    }



    @Step("Go to Main Menu from any place.")
    public boolean goToMainMenu() {
        while (seCircledArrow.exists()) {
            step("`Circled Arrow button` (go previous menu) is present on page - it's not Main menu");
            seCircledArrow.click();
        }
        boolean bNoCircledArrowButton = !seCircledArrow.exists();
        step("In Main Menu:" + bNoCircledArrowButton);
        infoShot("In Main Menu:" + bNoCircledArrowButton);
        return bNoCircledArrowButton;
    }



}
