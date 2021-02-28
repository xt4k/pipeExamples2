package app.helper;

import app.utils.CommonOperations;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
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
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.lang.Thread.currentThread;
import static org.testng.Assert.assertEquals;

public class BaseTestGui {
    public static final Logger LOG = LogManager.getLogger(BaseTestGui.class);
    public static String sqlDate = "yyyy-MM-dd";
    public static String sqlDateTimeLong = "yyyy-MM-dd HH:mm:ss.SSS";
    private LogEntries logEntries;

    @Step("Info screenshot:`{0}`.")
    public static void infoShot(String sMessage) {
        step(format("Info screenshot: `%s`.", sMessage));
        attachPageScreenShot(sMessage);
        reportInfo(sMessage);
    }

    @Step("Assert Info:`{0}` is equal to expected.")
    public static void assertToReport(String actual, String expected, String errorMessage) {
        //public static void assertToReport(String actual, String expected, String sAssertType, String errorMessage) {
        // actual = actual.toUpperCase();
        switch (expected.toUpperCase()) {
            case "F":
                expected = "false".toUpperCase();
                break;
            case "T":
                expected = "true".toUpperCase();
                break;
        }
        assertEquals(actual.toUpperCase(), expected.toUpperCase(), errorMessage);
        reportInfo(format("Reporting result of compare actual value:`%s` with expected:`%s`", actual, expected));
    }

    private static String getSessionId() {
        return ((RemoteWebDriver) getWebDriver()).getSessionId().toString();
    }

    @Parameters({ "browser", "timeout" })
    @BeforeSuite(description = "Initial configuration before every TestSuite (Set System variables for test suite, open browser).", alwaysRun = true)
    public void suiteSetup(@Optional("chrome") String setBrowser, @Optional("10000") String timeOut) {
        String startTime = new CommonOperations().getAccurateTime();
        setProperty(("test.start.time"), startTime);
        reportInfo(format("Initial time:'%s'", startTime));
       /*    }
    @Parameters({ "browser", "timeout", "loginRow" })
    @BeforeClass(description = "Initial configuration before every class (Set System variables for test suite, open browser).", alwaysRun = true)
    public void baseSetUp(@Optional("chrome") String sBrowser, @Optional("10000") String timeOut, @Optional("1") String loginRow) {*/
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        String commonPropsPath = getProperty("common.cfg");
        String aQaTeacherPath = getProperty("aqa.teacher.cfg");
        String msgPath = getProperty("messages.cfg");
          reportInfo("commonPropsPath: "+commonPropsPath+";  aQaTeacherPath: "+aQaTeacherPath);
        Properties properties = new Properties();
        Properties propAqaTeacher = new Properties();
        Properties propMsg = new Properties();

        try { //-------------------------------
            reportInfo("get common.cfg file via maven compilation (release settings)");
            properties.load(new FileReader(commonPropsPath));
            propAqaTeacher.load(new FileReader(aQaTeacherPath));
            propMsg.load(new FileReader(msgPath));
            //-----------------------------------------------------------
        } catch (IOException e) {
            e.printStackTrace();
        }
        setProps(properties);
        setProps(propAqaTeacher);
        setProps(propMsg);


        reportInfo("default_config_selenide_browser_size:" + browserSize);//seConfig.browserSize());   //browser = sBrowser;
        if (getProperty("browser.size") != null && !getProperty("browser.size").isEmpty())
            browserSize = getProperty("browser.size");
        else {
            browserSize = "1920x1080";
            setProperty("selenide.browserSize", browserSize);
        }
        reportInfo(format("final selenide.browserSize/browserSize: %s/%s", getProperty("selenide.browserSize"), browserSize));
        //reportInfo("selenide configuration:" + browserCapabilities);
        screenshots = true;
        savePageSource = false;
        setProperty("selenide.savePageSource", valueOf(savePageSource));
        startMaximized = false;
        timeout = Long.parseLong(timeOut);
        browser = setBrowser;

        if (getProperty("app.srv.ip") != null && !getProperty("app.srv.ip").isEmpty())
            setProperty("server.ip.addr", getProperty("app.srv.ip"));

        baseUrl = "http://" + getProperty("server.ip.addr");

        headless = false;//true;//false;
        open(baseUrl);
        fastSetValue = true;//default - false
        savePageSource = false;
        // logEntries = getWebDriver().manage().logs().get(valueOf(LogType.BROWSER));
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
        reportInfo(format("Started test:`%s`.", method.getName())); //==    Logs logs = WebDriverRunner.getWebDriver().manage().logs();
        //  logEntries = getWebDriver().manage().logs().get(valueOf(LogType.BROWSER));
    }


    @AfterMethod(description = "Test completion fixture. For failed cases: Add page screenshot, then navigate to initial point.", alwaysRun = true)
    public void afterTest(ITestResult result) {
        try {
            String text = testResultMsg(result);
            LOG.info(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        reportInfo("Close test suite.");
    }


    @Step("Test:'{1}' result message generation.")
    protected String testResultMsg(ITestResult result) {
        String testResult = displayStatus(result.getStatus());
        String sDuration = valueOf((result.getEndMillis() - result.getStartMillis()) / 1000);
        String sMessage = format(
                "\n********************************************************************************************************\n" +
                        "Auto-test : `%s`, Duration:`%s` sec; Test Status:`%s` (path:`%s`)." + "\n*****************" +
                        "***************************************************************************************.",
                result.getName(), sDuration, testResult, result.getInstanceName());
        return sMessage;
    }

    public static String getMethodName() {
        return currentThread().getStackTrace()[2].getMethodName() + " ";
    }

    private static String displayStatus(int status) {
        if (ITestResult.SKIP == status) {
            return "SKIPPED";
        } else if (ITestResult.SUCCESS == status) {
            return "PASSED";
        } else if (ITestResult.FAILURE == status) {
            return "FAILED";
        } else if (ITestResult.SUCCESS_PERCENTAGE_FAILURE == status) {
            return "SUCCESS_PERCENTAGE_FAILURE";
        } else {
            return "UNKNOWN_STATUS";
        }
    }


}