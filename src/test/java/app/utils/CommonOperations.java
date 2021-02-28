package app.utils;

import app.helper.BaseTestGui;
import io.qameta.allure.Step;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static app.pageobject.BasePageObject.reportInfo;
import static com.codeborne.selenide.Configuration.*;
import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

public class CommonOperations extends BaseTestGui {
    private static Random rand = new Random();

    public String getRandomMultiDigitNumber(int counter) {
        String randNum ="";
        for (int i=1;i<=counter;i++)  randNum=randNum+(new Random().nextInt(10)); // step("randNum: " + randNum);
        return randNum;
    }




    // @Step("Returns random integer with top bound: '10^7+123456'")
    public int iGetRandomInt() {
        // then 3 insert  instead of 99997 - use randomized number
        //  Random randomized = new Random(); // Print next int value        // Returns number between 0-9
        // int randomDocumentId = randomized.nextInt(20000000) + 123456;
        int iRandomNum = (rand.nextInt(10_000_000) + 123456);
        //step("Returned random number: " + iRandomNum);
        return iRandomNum;//Integer.MAX_VALUE);
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
                    TimeUnit.MILLISECONDS.toHours(lMillis),
                    TimeUnit.MILLISECONDS.toMinutes(lMillis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(lMillis)), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(lMillis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lMillis)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        reportInfo(String.format("`%s` Test duration: `%s`.", getMethodName(), sDuration));
        return sDuration;
    }


    // @Step("Get `6-Digit` unique day-time index.")
    public String sGetUnique6digitTimeIndex() {
        long lMilliSeconds = new Date().getTime();//   step("milliseconds:"+lMilliSeconds);
        String sMilliseconds = String.valueOf(lMilliSeconds);
        String sNds = sMilliseconds.substring(sMilliseconds.length() - 2);
        String sIndex = new SimpleDateFormat("HHmm").format(new Date()) + sNds;
        //------------------------------------
        //step("Unique 9 digit time-index: " + sIndex);
        return sIndex;
    }


    @Step("Get precious time.")
    public String getAccurateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String sStartTime = formatter.format(new Date());
        reportInfo("Precious time is: " + sStartTime);
        return sStartTime;
    }

    @Step("Convert localdate: '{0}' into String by pattern: '{1}'.")
    public String sLocalDateToStringBy(LocalDate localDate, String sFormatPattern) {
        DateTimeFormatter formatter = ofPattern(sFormatPattern);
        String stringDate = localDate.format(formatter);
        reportInfo(format("local date:'%s' =>:'%s' (by pattern:'%s')", localDate, stringDate, sFormatPattern));
        return stringDate;
    }

    @Step("Erase all special symbol in String:'{0}', append sPrefix:'{1}', then return (atomic).")
    public String eraseSpecialSymbolInString(String sString, String sPrefix) {
        String clearedText = sString.replaceAll("[^A-Za-z0-9]", "");
        String sFinalText = sPrefix + clearedText;//fieldname in cw_tsMap
        reportInfo(format("Converted:'%s' => '%s'.", sString, sFinalText));
        return sFinalText;
    }

    @Step("Get String :'{0}', erase all NON-DIGIT symbol, add Prefix: '{1}', then return) (atomic).")
    public String leftOnlyNumbersInString(String sString, String sPrefix) {
        String digits = sString.replaceAll("[^0-9]", "");
        String extendedDigits = sPrefix + digits;//fieldname in cw_tsMap
        reportInfo(format("Converted:'%s' into :'%s':", sString, extendedDigits));
        return extendedDigits;
    }

    @Step("Convert Date String: '{0}' into format '{1}'.")// 'D-M-YYYY'.")
    public String sConvertStringYYYY_MM_ddToFormat(String sInDate, String sPattern) {//        reportInfo("Convert String");
        SimpleDateFormat formatter = new SimpleDateFormat(sPattern);
        SimpleDateFormat formatter2 = new SimpleDateFormat("ddMMyyyy");

        Date date = null;
        try {
            date = formatter2.parse(sInDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sDate = formatter.format(date);
       /*
        reportInfo(String.format("Convert String:'%s' into '%s'.", sInDate, sPattern));
        String middleDate =formatter2.format(sInDate);
        reportInfo(String.format("middledate:'%s'.", middleDate));
        String sDate = formatter.format(middleDate);*/
        reportInfo(format("Converted Date String 'YYYY-MM-dd':'%s' by pattern '%s' into:'%s'.", sInDate, sPattern, sDate));
        return sDate;
    }

    @Step("Convert String:'{0}' with delimiters: '{1}' and '{2}' to HashMap form and return.")
    public Map<String, String> mConvertStringToHashMap(String sAttribute, String sParametersDelimiter, String sNameValueDelimiter) {
        Map<String, String> hm = new HashMap<>();
        String[] aPairs = sAttribute.split(sParametersDelimiter);
        for (String sPair : aPairs) {
            String[] aKeyValue = sPair.split(sNameValueDelimiter);
            hm.put(aKeyValue[0], aKeyValue[1]);
        }
        reportInfo(format("String:'%s' converted into hashmap:'%s'.", sAttribute, singletonList(hm)));
        return hm;
    }

    @Step("Convert violation text to map of violated and not-violated MLC rules.")
    public Map<String, String> getMlcRulesViolate(String sViolations, boolean bIsWk) {
        String sActualRulesViolations = "";
        String sNoViolatedRules = "";
        String[] aRules;
        Map<String, String> mRules = new HashMap<>();
        if (bIsWk) { //String[] aRulesWk = { "a", "b_not-wk", "b_wk", "c", "d", "e", "f" };}
            aRules = new String[]{ "a", "b_wk", "c", "d", "e", "f" };
        } else {
            aRules = new String[]{ "a", "b_not-wk", "c", "d" };
        }
        //int iRules = aRules.length;
        //for (int i = 0; i < iRules; i++) {
        for (String aRule : aRules) {
            String sSystemRuleMessageName = ("mlc.rule." + aRule.toLowerCase() + ".violation.message").toLowerCase();
            String sRuleViolationMsg = getProperty(sSystemRuleMessageName);
            reportInfo(String.format("System (hardcoded) rule:'%s' violation message is:'%s'.", sSystemRuleMessageName, sRuleViolationMsg));
            if (sViolations.contains(sRuleViolationMsg)) {//check if hint messages is contain text for shown rule violations.
                sActualRulesViolations = (sActualRulesViolations + aRule.substring(0, 1)).toUpperCase();
                reportInfo(String.format("In timecard title message present text for MLC rule(s):'%s' violation.", sActualRulesViolations));
            } else {
                sNoViolatedRules = (sNoViolatedRules + aRule.substring(0, 1)).toUpperCase();
                reportInfo(String.format("Hint message doesn't contain information about one of rule(s) violation:'%s' violation.", sNoViolatedRules));
            }
        }
        mRules.put("sActualRulesViolated", sActualRulesViolations);
        mRules.put("sActualRulesNotViolated", sNoViolatedRules);

        reportInfo(String.format("Actual violated MLC Rule(s):'%s' and Not-violated rule(s):'%s'.",
                mRules.get("sActualRulesViolated"), mRules.get("sActualRulesNotViolated")));

        return mRules;
    }

    @Step("Convert Date String: `{0}` into format `{1}`.")// 'D-M-YYYY'.")
    public String sConvertLocalDateToStringMonthName_YYYY(String sInDate, String sPattern) {//        reportInfo("Convert String");
        SimpleDateFormat formatter = new SimpleDateFormat(sPattern);
        SimpleDateFormat formatter2 = new SimpleDateFormat("ddMMyyyy");

        Date date = null;
        try {
            date = formatter2.parse(sInDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sDate = formatter.format(date);
       /*
        reportInfo(String.format("Convert String:'%s' into '%s'.", sInDate, sPattern));
        String middleDate =formatter2.format(sInDate);
        reportInfo(String.format("middledate:'%s'.", middleDate));
        String sDate = formatter.format(middleDate);*/
        step(String.format("Converted Date String 'YYYY-MM-dd':'%s' by pattern '%s' into:'%s'.", sInDate, sPattern, sDate));
        return sDate;
    }

    @Step("Convert String: Into multi values format: Stable parameter:`{0}`, Multi-Parameter:`{1}`.")
    public String convertToMultiValuesSqlPairConstVar(String sSubTeamSequenceNoId, String sPinString) {
        String sResultString;
        // String sInitial =String.format("( %s, %s )",sSubTeamSequenceNoId);
        String[] aPins = sPinString.split(",");
        StringBuilder sResultStringBuilder = new StringBuilder();
        for (String sPin : aPins)
            sResultStringBuilder.append(String.format("(%s, %s),", sSubTeamSequenceNoId, sPin));
        sResultString = sResultStringBuilder.deleteCharAt(sResultStringBuilder.lastIndexOf(",")).toString();
        reportInfo("Final string: " + sResultString);
        // String s = "        ( 10142333, 1617 ), ( 10142333, 2579), ( 10142333, 3105),( 10142333, 20072 ), ( 10142333,22210 )";
        return sResultString;
    }

    @Step("Convert String:{0} with current pattern:`{1}` into expected: {2} (atomic).")
    public String sConvertDateTimeToExpectedFormat(String sDateTime, String sCurrentPattern, String sExpectedPattern) {
        //   String sPatternOnPage = sSqlDateTimeShort;     //   String sPatternOnPage = "dd-MM-YYYYHH:mm";
        //step(String.format("convert",sDateTime,sCurrentPattern,sExpectedPattern));
        SimpleDateFormat sdfInput = new SimpleDateFormat(sCurrentPattern);
        Date dateConvertDateTime = null;
        try {
            dateConvertDateTime = sdfInput.parse(sDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdfOutput = new SimpleDateFormat(sExpectedPattern);
        String sConvertedDateTime = sdfOutput.format(dateConvertDateTime);

        String sLog = String.format("Incoming string:`%s` converted to:`%s` (from actual pattern:`%s` to expected:`%s`)."
                , sDateTime, sConvertedDateTime, sCurrentPattern, sExpectedPattern);
        reportInfo(sLog);
        return sConvertedDateTime;
    }


    // @Step("Convert String:{0} (pattern:`{1}`) into 'LocalDate' object (atomic).")
    public LocalDate ldStringToLocalDate(String sDate, String sPattern) {
        LocalDate ldParsed = LocalDate.parse(sDate, ofPattern(sPattern));
        // reportInfo(format("`String`:`%s` => `LocalDate`:`%s`", sDate, ldParsed));// System.out.println("sDate: " + sDate);
        return ldParsed;
    }

    //  @Step("Convert String:{0} (pattern:`{1}`) into 'LocalDateTime' object (atomic).")
    public LocalDateTime ldtStringToLocalDateTime(String sDate, String sPattern) {
        sDate = sDate.substring(0, sPattern.length());
        LocalDateTime ldtParsed = LocalDateTime.parse(sDate, ofPattern(sPattern));
        //reportInfo(format("`String`:`%s` => `LocalDateTime`:`%s`", sDate, ldtParsed));
        return ldtParsed;
    }

    @Step("Get time (according patterns:`{0}`).")
    public String sGetDateTime(String sPattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(sPattern);
        String sTime = formatter.format(new Date());
        reportInfo(getMethodName() + " returns: " + sTime);
        return sTime;
    }

    @Step("Get time (according:`sSqlDateTimeLong`).")
    public String sGetDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(sqlDateTimeLong);
        String sTime = formatter.format(new Date());
        reportInfo(getMethodName() + " returns: " + sTime);
        return sTime;
    }


    @Step("Get string of random date in period:`{1}--{2}` in `{0}` format.")
    public String sGetRandomDate(String sMinDate, String sMaxDate, String sPattern) {
        // LocalDate startDate = LocalDate.of(1990, 1, 1);
        LocalDate ldStartDate = ldStringToLocalDate(sMinDate, sPattern);
        long lStart = ldStartDate.toEpochDay();

        //LocalDate ldEndDate = LocalDate.now(); //end date
        LocalDate ldEndDate = ldStringToLocalDate(sMaxDate, sPattern);
        long lEnd = ldEndDate.toEpochDay();

        long lRandomEpochDay = ThreadLocalRandom.current().longs(lStart, lEnd).findAny().getAsLong();
        LocalDate ldRandom = LocalDate.ofEpochDay(lRandomEpochDay);
        String sResultDate = sLocalDateToStringBy(ldRandom, sPattern);
        reportInfo(getMethodName() + " returns: " + sResultDate);
        return sResultDate;
    }

    //@Step("Get random date in period:`{0}--{1}`.")
    public LocalDate ldGetRandomLocalDate(String sMinDate, String sMaxDate) {
        LocalDate ldStart = ldStringToLocalDate(sMinDate, sqlDate);
        long lStart = ldStart.toEpochDay();
        LocalDate ldEnd = ldStringToLocalDate(sMaxDate, sqlDate);
        long lEnd = ldEnd.toEpochDay();
        long lDuration = lEnd - lStart;
        int iPeriod = Integer.parseInt(String.valueOf(lDuration));
        long lRand = lStart + new Random().nextInt(iPeriod);
        LocalDate ldRand = LocalDate.ofEpochDay(lRand);
        long lRandomEpochDay = ThreadLocalRandom.current().longs(lStart, lEnd).findAny().getAsLong();
        LocalDate ldRandSo = LocalDate.ofEpochDay(lRandomEpochDay);
        //step(String.format("`%s` =>`%s` (by SO:`%s`) in:`%s` format.", getMethodName(), ldRand, ldRandSo, sSqlDate));
        return ldRandSo;
    }

  /*  @Step("Get random date in period:`{1}--{2}` in `{0}` format.")
    public LocalDate ldGetRandomLocalDate(LocalDate ldMinDate, LocalDate ldMaxDate, String sPattern) {
        long lStart = ldMinDate.toEpochDay();
        long lEnd = ldMaxDate.toEpochDay();
        long lDuration = lStart - lEnd;
        int iPeriod = Integer.parseInt(String.valueOf(lDuration));
        long lRand = lStart + new Random().nextInt(iPeriod);
        LocalDate ldRand = LocalDate.ofEpochDay(lRand);
        long lRandomEpochDay = ThreadLocalRandom.current().longs(lStart, lEnd).findAny().getAsLong();
        LocalDate ldRandSo = LocalDate.ofEpochDay(lRandomEpochDay);
        String sLogMsg = String.format("`%s` =>`%s` (by SO:`%s`) in:`%s` format.", getMethodName(), ldRand, ldRandSo, sPattern);
        reportInfo(sLogMsg);
        return ldRandSo;
    }
      @Step("Get random date in period days:`{1}--NOW--{2}` in `{0}` format.")
    public LocalDate ldGetRandomLocalDate(int iMinDate, int iMaxDate, String sPattern) {

        long lStart = LocalDate.now().minusDays(iMinDate).toEpochDay();
        long lEnd = LocalDate.now().plusDays(iMaxDate).toEpochDay();
        long lDuration = lStart - lEnd;
        int iPeriod = Integer.parseInt(String.valueOf(lDuration));
        long lRand = lStart + new Random().nextInt(iPeriod);
        LocalDate ldRand = LocalDate.ofEpochDay(lRand);
        long lRandomEpochDay = ThreadLocalRandom.current().longs(lStart, lEnd).findAny().getAsLong();
        LocalDate ldRandSo = LocalDate.ofEpochDay(lRandomEpochDay);
        String sLogMsg = String.format("`%s` =>`%s` (by SO:`%s`) in:`%s` format.", getMethodName(), ldRand, ldRandSo, sPattern);
        reportInfo(sLogMsg);
        return ldRandSo;
    }

    @Step("Get Date&Time String.")
    public String sGetUniqueDateAndTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy_HHmmss");
        String sIndex = formatter.format(new Date());
        reportInfo(getMethodName() + ": " + sIndex);
        return sIndex;
    }
*/

    @Step("Convert datetime string:`{0}`.")
    public String sChangeDateTimeFormat(String sDateTimeIn, String sInPattern, String sOutPattern) {
        LocalDateTime ldt = LocalDateTime.parse(sDateTimeIn, ofPattern(sInPattern));
        String sOutDateTime = ldt.format(ofPattern(sOutPattern));
        reportInfo(format("%s:`%s` =>:`%s`", getMethodName(), sDateTimeIn, sOutDateTime));
        return sOutDateTime;
    }


    @Step("Convert datetime:`{0}`=> String by:`{1}`.")
    public String sLocalDateTimeToStringBy(LocalDateTime ldtDateTime, String sFormatPattern) {
        String sDateTime = ldtDateTime.format(ofPattern(sFormatPattern));
        reportInfo(format("local date-time:'%s' =>:'%s' (by pattern:'%s')", ldtDateTime, sDateTime, sFormatPattern));
        return sDateTime;
    }

    //  @Step("Random crew gender.")
    public String sGender() {
        String sGender = "F";
        if (new Random().nextBoolean()) sGender = "M";//        step("crew gender: " + sGender);
        return sGender;
    }

    public String sOppositeGender(String sGender) {
        String sOppositeGender;
        if (sGender.toUpperCase().equals("M") || sGender.toUpperCase().equals("MALE")) sOppositeGender = "F";
        else if (sGender.toUpperCase().equals("F") || sGender.toUpperCase().equals("FEMALE")) sOppositeGender = "M";
        else sOppositeGender = "Alien";
        step("Opposite gender: " + sOppositeGender);
        return sOppositeGender;
    }

    public String sGetRandomLetter() {
        Random r = new Random();
        String sChar = Character.toString((char) (r.nextInt(26) + 'a'));
        step("Random character: " + sChar);
        return sChar;
    }



}