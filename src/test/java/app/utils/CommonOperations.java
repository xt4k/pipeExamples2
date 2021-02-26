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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Step("Convert timecard registration block Title (with violations messages):'{0}' into only violations text.")
    public String convertTimecardBlockViolationsInTitle(String sElementTitleAll, String sPrefix, String sSuffix) {
        String[] sViolatedTitleCutted1 = sElementTitleAll.split(sPrefix);
        String[] sViolatedTitleCutted2 = sViolatedTitleCutted1[1].split(sSuffix);
        String sViolationText = sViolatedTitleCutted2[0];//.toLowerCase();/// here changes
        reportInfo(String.format("Title (with violations messages):'%s'=> only violation text:'%s'.", sElementTitleAll, sViolationText));
        return sViolationText;
    }

    @Step("Convert timecard registration block Title:'{0}' into string array.")
    public String[] convertTimecardBlockTitleToStringArray(String elementTitleAll) {
        String elementTitleAllmod = elementTitleAll
                .replaceAll("<div>", ";")
                .replaceAll("</div>", ";")
                .replaceAll(": ", ";");//     System.out.println("elementTitleAll2: "+elementTitleAllmod);
        elementTitleAllmod = elementTitleAllmod.replaceAll(";;", ";");// System.out.println("elementTitleAll2: "+elementTitleAllmod);
        String[] titleBlocks = elementTitleAllmod.split(";");
        reportInfo(String.format("String:'%s' converted into list:'%s'.", elementTitleAll, Arrays.toString(titleBlocks)));

        return titleBlocks;
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
            ofNullable(getProperty("app.server.ip")).ifPresent(s -> props.setProperty("APP server ip", s));
            ofNullable(browserVersion).ifPresent(s -> props.setProperty("Browser", s));
            ofNullable(getProperty("db.name")).ifPresent(s -> props.setProperty("DB", s));
            ofNullable(getProperty("java.version")).ifPresent(s -> props.setProperty("Java version", s));
            // ofNullable(getProperty("webdriver.chrome.driver")).ifPresent(s -> props.setProperty("webdriver.chrome.driver", s));
            ofNullable(getProperty("app.version")).ifPresent(s -> props.setProperty("APP version", s));
            ofNullable(getProperty("test.start.time")).ifPresent(s -> props.setProperty("Test started", s));
            props.setProperty("Test completed", endTime);
            ofNullable(headless).ifPresent(s -> props.setProperty("Browser headless mode", String.valueOf(s)));
            props.setProperty("Browser resolution", browserSize);
            String sTestDuration = sGetDuration(getProperty("test.start.time"), endTime);
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

    private String sGetDuration(String sStartTime, String sEndTime) {
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

    @Step("Convert String 'ddMMyyyy'': '{0}' into 'yyyy-MM-dd'.")
    String sConvertStringDDMMYYYYtoYYYY_MM_DD(String sInitialDate) {
        DateTimeFormatter dtfFormatter1 = ofPattern("ddMMyyyy");
        DateTimeFormatter dtfFormatter2 = ofPattern(sSqlDate);        //System.out.println(LocalDate.parse(sInitialDate, dtfFormatter1).format(dtfFormatter2));
        // reportInfo(String.format("initial string: %s, dtfFormatter1 1:'%s', dtfFormatter1 2:'%s' ", sInitialDate, dtfFormatter1, dtfFormatter2));
        String sDateString = parse(sInitialDate, dtfFormatter1).format(dtfFormatter2);        //System.out.println("formatted date:" + sDateString);
        reportInfo(String.format("String: In:'%s' => Out:'%s'.", sInitialDate, sDateString));
        return sDateString;
    }

    @Step("Convert String 'Time': '{0}' from 'HHMMSS' into 'HH:MM:SS' string.")
    String sConvertStringHHMMtoHH_MM_SS(String sTime) {
        String modifiedTime = sTime.substring(0, 2) + ":" + sTime.substring(2, 4) + ":" + sTime.substring(4);
        reportInfo("Return Time string (HH:MM:SS): " + modifiedTime);
        return modifiedTime;
    }

    @Step("Convert String 'Time' {0} from 'HHMM' into 'HH:MM' string.")
    String sConvertStringHHMMtoHH_MM(String sTime) {
        String modifiedTime = sTime.substring(0, 2) + ":" + sTime.substring(2);
        reportInfo("Return Time string (HH:MM): " + modifiedTime);
        return modifiedTime;
    }

    @Step("Convert String 'ddMMyyyy': '{0}' into 'LocalDate' object 'yyyy-MM-dd'")
    public LocalDate ldConvertStringToDateYYYY_MM_DD(String sDate) {
        DateTimeFormatter formatter = ofPattern("ddMMyyyy");
        DateTimeFormatter formatter2 = ofPattern(sSqlDate);
        LocalDate convertedLocalDate = parse(parse(sDate, formatter).format(formatter2));
        reportInfo("Return Date LocalDate (yyyy-MM-dd): " + convertedLocalDate);    //System.out.println(LocalDate.parse(sDate, formatter).format(formatter2));
        return convertedLocalDate;
    }

    @Step("Convert String 'ddMMyyyy' {0} into 'LocalDate' object (atomic).")
    public LocalDate ldConvertStringDDMMyyyyToLocalDate(String initialDate) {
        DateTimeFormatter formatter = ofPattern("ddMMyyyy");
        LocalDate localDate = LocalDate.parse(initialDate, formatter);
        reportInfo(format("String 'Date':'%s' converted  into 'LocalDate': '%s'", initialDate, localDate));// System.out.println("initialDate: " + initialDate);
        return localDate;
    }

    @Step("Convert String 'Date' {0}  into 'LocalDate' object.")
    public LocalDate convertStringDD_MM_yyyyToLocalDate(String initialDate) {
        DateTimeFormatter formatter = ofPattern(sUiDateShort);
        LocalDate localDate = LocalDate.parse(initialDate, formatter);
        reportInfo(format("String 'Date':'%s' converted  into 'LocalDate': '%s'", initialDate, localDate));// System.out.println("initialDate: " + initialDate);
        return localDate;
    }

    @Step("Convert 'timeblock hint title': '{0}' (excluding tag:'{1}' by splitter:'{2}') into returned string array.")
    public String[] convertSoleWorkTypeTimeBlockHintToArray(String sHint, String sTag, String sSplitter) {
        //soleWorkTypeTimeBlockHint = getSoleWorkTypeTimeBlockHint(); //String sHint = soleWorkTypeTimeBlockHint.getAttribute("title");
        String sTitleWorktypeAndTimePoints = null;
        String patternString = "<" + sTag + ">(.*?)</" + sTag + ">";
        Pattern pattern = Pattern.compile(patternString);//"<div>(.*?)</div>");
        Matcher m = pattern.matcher(sHint);
        if (m.find()) {
            sTitleWorktypeAndTimePoints = m.group(1);
        }
        assert sTitleWorktypeAndTimePoints != null;
        String[] aStrings = sTitleWorktypeAndTimePoints.split(sSplitter);//": |-");
        reportInfo(format("Worktype timeblock sHint:  HTML text: '%s'.\n it first string: '%s' converted into array: '%s'.",
                sHint, sTitleWorktypeAndTimePoints, Arrays.toString(aStrings)));
        return aStrings;
    }


    @Step("Generate unique time-based index (atomic).")
    public String sGetFlagEvenOrOdd(String sNumberIndex) {
        int iNumberIndex = Integer.parseInt(sNumberIndex);
        String sResult;
        if (iNumberIndex % 2 == 0)
            sResult = "E";//even
        else
            sResult = "O";//odd
        return sResult;
    }

 /*   // @Step("Get `6-Digit` unique day-time index.")
    public String sGetUnique6digitTimeIndex() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        String sIndex = formatter.format(new Date());//        step("Set unique day-time index: " + sIndex);
        return sIndex;
    }*/

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


    public int iGetUnique6digitTimeIndex() {
        int iIndex = Integer.parseInt(sGetUnique6digitTimeIndex());//        step("Set unique day-time index: " + iIndex);
        return iIndex;
    }
/*

    public String sGetUnique7digitTimeIndex() {
        long lMilliSeconds = new Date().getTime();//   step("milliseconds:"+lMilliSeconds);
        String sMilliseconds = String.valueOf(lMilliSeconds);
        String sNds = sMilliseconds.substring(sMilliseconds.length() - 3);
        String sIndex = new SimpleDateFormat("HHmm").format(new Date()) + sNds;
        //------------------------------------
        //step("Unique 9 digit time-index: " + sIndex);
        return sIndex;
    }
*/


    public String sGetUnique7digitTimeIndex() {
        String sIndex = String.valueOf(new Random().nextInt(9999999));
        step("Index: " + sIndex);
        return sIndex;
    }


    public String sGetRandomMultiDigitNumber(int counter) {
        String randNum ="";
        for (int i=1;i<=counter;i++)  randNum=randNum+(new Random().nextInt(10)); // step("randNum: " + randNum);
        return randNum;
    }



    public String sGetUnique8digitTimeIndex() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        // String sIndex = formatter.format(new Date());//.substring(0,8);
        //-----------------------------------------
        //  Instant inst = Instant.now();
        long lMilliSeconds = new Date().getTime();//   step("milliseconds:"+lMilliSeconds);
        String sMilliseconds = String.valueOf(lMilliSeconds);
        String sNds = sMilliseconds.substring(sMilliseconds.length() - 2);
        String sIndex = formatter.format(new Date()) + sNds;
        //------------------------------------
        //step("Unique 9 digit time-index: " + sIndex);
        return sIndex;
    }


    // @Step("Get `7-Digit` unique day-time index.")
    public String sGetUnique9digitTimeIndex2() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        long lMilliSeconds = new Date().getTime();//   step("milliseconds:"+lMilliSeconds);
        String sMilliseconds = String.valueOf(lMilliSeconds);
        String sNds = sMilliseconds.substring(sMilliseconds.length() - 3);
        String sIndex = formatter.format(new Date()) + sNds;
        //------------------------------------
        //step("Unique 9 digit time-index: " + sIndex);
        return sIndex;
    }


    public String sGetUnique9digitTimeIndex() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");

        String random3digit = (new Random().nextInt(10)) + String.valueOf(new Random().nextInt(10)) + new Random().nextInt(10);
        String timeIndex = formatter.format(new Date());
        String randomIndex = timeIndex + random3digit;
        //step(String.format("Generated random 9 digit number: %s (time: %s, random: %s).", randomIndex, timeIndex, random3digit));
        return randomIndex;
    }

  /*  @Step("Get 10 digit time index.")
    public int sGetRandom10DigitTimeIndex() {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmssSSS");
        long lo = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        String sMilliseconds = String.valueOf(lo);
        String sNds = sMilliseconds.substring(sMilliseconds.length()-3);
        String sIndex = formatter.format(new Date());
        int iIndex = Integer.parseInt(sIndex);
        reportInfo(getMethodName() + ": " + sIndex);
        return iIndex;
    }*/


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
        SimpleDateFormat formatter = new SimpleDateFormat(sSqlDateTimeLong);
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
        LocalDate ldStart = ldStringToLocalDate(sMinDate, sSqlDate);
        long lStart = ldStart.toEpochDay();
        LocalDate ldEnd = ldStringToLocalDate(sMaxDate, sSqlDate);
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