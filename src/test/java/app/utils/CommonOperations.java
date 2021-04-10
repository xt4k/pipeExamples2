package app.utils;

import app.helper.BaseTestGui;
import io.qameta.allure.Step;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import static app.pageobject.BasePageObject.reportInfo;
import static com.codeborne.selenide.Configuration.*;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.*;

public class CommonOperations extends BaseTestGui {
    private static Random rand = new Random();

    public String getRandomMultiDigitNumber(int counter) {
        String randNum ="";
        for (int i=1;i<=counter;i++)  randNum=randNum+(new Random().nextInt(10)); // step("randNum: " + randNum);
        return randNum;
    }

    @Step("Create and fill environments.properties with information about environment")
    public void fSaveToEnvironmentFile() {
        FileOutputStream fos = null;
        try {//get from https://automated-testing.info/t/allure-peredacha-environment-v-papku-allure-result/7031/18
            // Properties props = new OrderedProperties();
            Properties props = new Properties();
            String endTime = new CommonOperations().getAccurateTime(); // reportInfo("test.end.time: "+endTime);
            ofNullable(baseUrl).ifPresent(s -> props.setProperty("APP url", s));
            ofNullable(getProperty("os.name")).ifPresent(s -> props.setProperty("APP server OS", s));

            ofNullable(browserVersion).ifPresent(s -> props.setProperty("Browser", s));
            ofNullable(getProperty("db.name")).ifPresent(s -> props.setProperty("DB", s));
            ofNullable(getProperty("java.version")).ifPresent(s -> props.setProperty("Java version", s));
            // ofNullable(getProperty("webdriver.chrome.driver")).ifPresent(s -> props.setProperty("webdriver.chrome.driver", s));
            ofNullable(getProperty("test.start.time")).ifPresent(s -> props.setProperty("Test started", s));
            props.setProperty("Test completed", endTime);
            ofNullable(headless).ifPresent(s -> props.setProperty("Browser headless mode", String.valueOf(s)));
            props.setProperty("Browser resolution", browserSize);
            String sTestDuration = getDuration(getProperty("test.start.time"), endTime);
            ofNullable(sTestDuration).ifPresent(s -> props.setProperty("Test duration (HH:MM:SS)", s));
            reportInfo("environment.path: " + getProperty("environment.path"));
            //fos = new FileOutputStream(getProperty("environment.out.path"));
            fos = new FileOutputStream(getProperty("environment.path"));
            props.store(fos, null);//"no comments");
            reportInfo("props file: " + props);
            fos.close();
        } catch (IOException e) {
            LOG.error("IO problem when writing allure properties file", e);
        }/* finally {
            closeQuietly(fos);
        }*/
    }

    private String getDuration(String sStartTime, String sEndTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        String sDuration = null;
        try {
            Date dateStart = sdf.parse(sStartTime);
            Date dateEnd = sdf.parse(sEndTime);
            long lMillis = dateEnd.getTime() - dateStart.getTime();

            reportInfo(format("`%s`, start-end:%s-%s in ms duration: `%s`.", getMethodName(), dateStart, dateEnd, lMillis));
            sDuration = format("%02d:%02d:%02d",
                    MILLISECONDS.toHours(lMillis),
                    MILLISECONDS.toMinutes(lMillis) -
                            HOURS.toMinutes(MILLISECONDS.toHours(lMillis)), // The change is in this line
                    MILLISECONDS.toSeconds(lMillis) -
                            MINUTES.toSeconds(MILLISECONDS.toMinutes(lMillis)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        reportInfo(String.format("`%s` Test duration: `%s`.", getMethodName(), sDuration));
        return sDuration;
    }
    @Step("Get precious time.")
    public String getAccurateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String sStartTime = formatter.format(new Date());
        reportInfo("Precious time is: " + sStartTime);
        return sStartTime;
    }

}