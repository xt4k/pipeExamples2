package app.helper;

import app.pageobject.BasePageObject;
import app.pageobject.MainPageObject;
import app.utils.CommonOperations;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import static app.pageobject.BasePageObject.attachPageScreenShot;
import static app.pageobject.BasePageObject.reportInfo;
import static com.codeborne.selenide.Configuration.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.lang.Thread.currentThread;
import static org.testng.Assert.assertEquals;

public class BaseTestGui {
    public static final Logger LOG = LogManager.getLogger(BaseTestGui.class);


    public static String sSqlDate = "yyyy-MM-dd";
    public static String sSqlDateTimeShort = "yyyy-MM-dd HH:mm";
    public static String sSqlDateTimeNorm = "yyyy-MM-dd HH:mm:ss";
    public static String sSqlDateTimeLonger = "yyyy-MM-dd HH:mm:ss.S";
    public static String sSqlDateTimeLong = "yyyy-MM-dd HH:mm:ss.SSS";
    public static String sUiDateShort = "dd-MM-yyyy";
    public static String sUiDateTimeNorm = "dd-MM-yyyy HH:mm";

    private LogEntries logEntries;

    //@Step("Return method name.")
    public static String getMethodName() {
        return currentThread().getStackTrace()[2].getMethodName() + " ";
    }
    /*
    ///==========================================
       ChromeOptions options = new ChromeOptions();
        String browserVersion = browserCapabilities.getVersion();
        String browserName = browserCapabilities.getBrowserName();
        Platform browserPlatform = Platform.getCurrent();
        String[] br_pl = browserPlatform.getPartOfOsName();
        System.out.println("br_pl: " + br_pl);
        Set<String> optionsNames = options.getCapabilityNames();
        reportInfo(String.format("Browser/version/platform/optionsNames '%s/%s/%s/%s'", browserName, browserVersion,optionsNames);

    } */

    @Step("Info screenshot:`{0}`.")
    public static void infoShot(String sMessage) {
        step(String.format("Info screenshot: `%s`.", sMessage));
        attachPageScreenShot(sMessage);
        reportInfo(sMessage);
    }

    @Step("AUTO-TEST SCREENSHOT:`{0}`.")
    public static void infoShot2(String sMessage) {
        new BasePageObject().locally_updated()
                .waitForLoaded();
        step(String.format("REFRESHed SCREEN: `%s`-SHOT.", sMessage));
        attachPageScreenShot(sMessage);
    }

    @Step("Assert Info:`{0}` is equal to expected.")
    public static void assertToReport(String sActual, String sExpected, String sErrorMessage) {
        //public static void assertToReport(String sActual, String sExpected, String sAssertType, String sErrorMessage) {
        // sActual = sActual.toUpperCase();
        switch (sExpected.toUpperCase()) {
            case "F":
                sExpected = "false".toUpperCase();
                break;
            case "T":
                sExpected = "true".toUpperCase();
                break;
        }
        assertEquals(sActual.toUpperCase(), sExpected.toUpperCase(), sErrorMessage);
        String sMessage = String.format("Reporting result of compare actual value:`%s` with expected:`%s`", sActual, sExpected);
        reportInfo(sMessage);
    }

  /*  public static boolean isUnix() {
        String os = getProperty("os.name").toLowerCase();  //System.out.println("isUnix: " + os);
        return (os.contains("nix") || os.contains("nux"));// || isWindows(); // linux or unix
    }*/

    private static String getSessionId() {
        return ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
    }

    @Step("Unzip Gender from Cap letter:`{0}`.")
    public static String sDetectGender(String sValue) {
        String sCabinGender;
        if (!(sValue == null)) {
            if (sValue.toUpperCase().equals("M")) sCabinGender = "Male";
            else if (sValue.toUpperCase().equals("F")) sCabinGender = "Female";
            else sCabinGender = "Alien";
        } else return null;
        String sLogMsg = String.format("`%s` convert: `%s` => `%s`.", getMethodName(), sValue, sCabinGender);
        reportInfo(sLogMsg);
        return sCabinGender;
    }

    @Parameters({ "browser", "timeout", "loginRow" })
    @BeforeSuite(description = "Initial configuration before every TestSuite (Set System variables for test suite, open browser).", alwaysRun = true)
    public void suiteSetup(@Optional("chrome") String sBrowser, @Optional("10000") String timeOut, @Optional("1") String loginRow) {



       /*    }
    @Parameters({ "browser", "timeout", "loginRow" })
    @BeforeClass(description = "Initial configuration before every class (Set System variables for test suite, open browser).", alwaysRun = true)
    public void baseSetUp(@Optional("chrome") String sBrowser, @Optional("10000") String timeOut,
                          @Optional("1") String loginRow) {*/
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        String sCommonPropertiesPath = getProperty("common.cfg"); // Read Config file
        String sMenuItemsListPath = getProperty("app.menu.items.cfg"); // Read Config file
        String sMlcRulesPath = getProperty("mlc.cfg"); // Read Config file
        String sMsgPath = getProperty("message.cfg"); // Read Config file
        //  reportInfo("sCommonPropertiesPath: "+sCommonPropertiesPath+";  sMenuItemsListPath: "+sMenuItemsListPath);
        Properties properties = new Properties();
        Properties propMenu = new Properties();
        Properties propMlcRules = new Properties();
        Properties propMsg = new Properties();
        try { //-------------------------------
            //String directPath = "C:\\Users\\yuriy\\IdeaProjects\\app-tests\\src\\main\\resources\\common.properties";
            //properties.load(new FileReader(directPath));//System.getProperty("common.cfg")));
            //reportInfo("get common.cfg file via direct path (debug mode): " + directPath);
            //-----------------------------------------------------------
            reportInfo("get common.cfg file via maven compilation (release settings)");
            properties.load(new FileReader(sCommonPropertiesPath));
            propMenu.load(new FileReader(sMenuItemsListPath));
            propMlcRules.load(new FileReader(sMlcRulesPath));
            propMsg.load(new FileReader(sMsgPath));
            //-----------------------------------------------------------
        } catch (IOException e) {
            e.printStackTrace();
        }
        setProps(properties);
        setProps(propMenu);
        setProps(propMlcRules);
        setProps(propMsg);


        reportInfo("default_config_selenide_browser_size:" + browserSize);//seConfig.browserSize());   //browser = sBrowser;
        if (getProperty("browser.size") != null && !getProperty("browser.size").isEmpty())
            browserSize = getProperty("browser.size");
        else {
            browserSize = "1920x1080";
            setProperty("selenide.browserSize", browserSize);
        }
        reportInfo(String.format("final selenide.browserSize/browserSize: %s/%s", getProperty("selenide.browserSize"), browserSize));
        //reportInfo("selenide configuration:" + browserCapabilities);

        screenshots = true;
        savePageSource = false;
        setProperty("selenide.savePageSource", String.valueOf(savePageSource));
        startMaximized = false;
        timeout = Long.parseLong(timeOut);

        if (getProperty("app.srv.ip") != null && !getProperty("app.srv.ip").isEmpty())
            setProperty("server.ip.addr", getProperty("app.srv.ip"));

        baseUrl = "http://" + getProperty("server.ip.addr") + getProperty("base.page.suffix");

        headless = true; //false;// true;
        open(baseUrl);
        fastSetValue = true;//default - false
        savePageSource = false;
        logEntries = getWebDriver().manage().logs().get(String.valueOf(LogType.BROWSER));

        reportInfo("baseUrl: " + baseUrl);
        //step("HERE ALL system props");
        //  getProperties().forEach((key, value) -> reportInfo(String.format("Sys.prop:`%s`=`%s`", key, value)));
        //new SQLUtils().bRestoreBD("C:\\temp\\db_backup\\2021_jan_backup");
    }

    @Step("Set system properties")
    private void setProps(Properties properties) {
        properties.forEach((key, value) -> setProperty((String) key, (String) value));//  reportInfo("properties: " + properties);
    }

    @BeforeMethod(description = "Start logging.")
    public void logTestStart(Method method, Object[] params) {
        reportInfo(String.format("Started test:`%s`.", method.getName()));// with parameters:`%s`",, Arrays.toString(params)));
        //==        Logs logs = WebDriverRunner.getWebDriver().manage().logs();
        logEntries = getWebDriver().manage().logs().get(String.valueOf(LogType.BROWSER));
    }

    @AfterMethod(description = "`Stop logging` method message.", alwaysRun = true)
    public void logTestStop(Method method) {
        reportInfo("Stopped method: " + method.getName());
        String sessionId = getSessionId();
        infoShot(getMethodName() + " session id: " + sessionId);
        // closeWebDriver();
        // if ("true".equals(System.getProperty("video.enabled"))) {
        //sleep(5000);
        //   reportInfo("");// attachAllureVideo(sessionId);
        //  }
    }

    @AfterSuite(alwaysRun = true)
    @Step("Create environment properties file and Close test suite.")
    public void testSuiteDown() {
        (new CommonOperations()).fSaveToEnvironmentFile();
        reportInfo("Create environment properties file");

        if (logEntries != null)
            for (LogEntry logEntry : logEntries) {
                reportInfo("logEntries: " + logEntry.getMessage());
            }
        if (new BasePageObject().bIsLoggedIn())
            new MainPageObject().logOff();
        reportInfo("Close test suite.");
    }

    @Step("Transform in String result message for completed tests.")
    protected String sGetTestResultInString(ITestResult result) {
        String sResult = String.format("result: \n.getInstanceName():'%s', .getName():'%s'," +
                        "\n  .getStartMillis():'%s',: .getEndMillis():'%s',\n .getStatus():'%s'.",
                result.getInstanceName(), result.getName(), result.getStartMillis(), result.getEndMillis(), result.getStatus());
        reportInfo(String.format("Test execution return result:'%s'", sResult));
        return sResult;
    }


    @Step("Test:'{1}' result message generation.")
    protected String getStringAboutTestResult(ITestResult result, String sTestLabel) {
        String sTestResultStatus;
        if (result.getStatus() == ITestResult.SUCCESS) sTestResultStatus = "PASSED";
        else if (result.getStatus() == ITestResult.FAILURE) sTestResultStatus = "FAILED";
        else if (result.getStatus() == ITestResult.SKIP) sTestResultStatus = "SKIPPED";
        else sTestResultStatus = "UNDEFINED";
        String sDuration = String.valueOf((result.getEndMillis() - result.getStartMillis()) / 1000);
        String resultInString = String.format(" Package:`%s`, Test:`%s`, Duration:`%s` sec, Status:`%s`.",
                result.getInstanceName(), result.getName(), sDuration, sTestResultStatus);
        String sMessage = String.format("Test Result: ********** Automated Test `%s`: `%s` **********. \n " +
                        "********** '%s' **********.",
                sTestLabel, sTestResultStatus, resultInString);
        //reportInfo(sMessage);
        return sMessage;
    }


}