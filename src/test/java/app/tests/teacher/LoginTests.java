package app.tests.teacher;

import app.helper.BaseTestGui;
import app.helper.pojo.Teacher;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static app.pageobject.BasePageObject.reportInfo;
import static java.lang.System.getProperty;

@Epic("MobyMax Automation.")
@Feature("Teacher")
@Story("Registration")

public class LoginTests extends BaseTestGui {
    private TeacherScenario teacherTest;

    @BeforeClass(description = "Pre-testset fixture.")
    public void preTestset() {
      }

    @BeforeMethod(description = "Pre-test fixture.")
    public void preTest() {
     ///   new DeleteCmds().deleteAutoTestCabinAndCrewTestObjects();//   reportInfo(getMethodName() + " method completed: ");
    }


    @Step("Navigation back To Test Initial point after failed test.)")
    private void backToTestInitialPointOnFail() {
        reportInfo(getMethodName() + " after failed test.");
    }

 /*   @AfterMethod(description = "Test completion fixture. For failed cases: Add page screenshot, then navigate to initial point.", alwaysRun = true)
    public void afterTest(ITestResult result) {
        try {
            String sResult = testResultMsg(result );
            LOG.info(sResult);
            if (result.getStatus() == ITestResult.SUCCESS) reportInfo("Auto-Test_Result: PASSED!!!!!! ");
            else if (result.getStatus() == ITestResult.FAILURE) {
                LOG.info("\nAuto-Test_Result: FAILED??????? ");
                infoShot("APP page state after failed test.");
                backToTestInitialPointOnFail();
            } else if (result.getStatus() == ITestResult.SKIP)
                reportInfo(" ********** Automated Test was skipped.**********");
        } catch (Exception e) {
            e.printStackTrace();
        }
        reportInfo(getMethodName() + " method completed: ");//+ bResult
    }*/

    @AfterClass(description = "Fixture: Navigate Main Menu after testset.")
    public void postTestSet() {
      //  new BasePageObject().goToMainMenu();
        infoShot(getMethodName());
    }

    @Test(groups = { "teacher", "register" }, description = "Test 01. Login as a Teacher .")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)  //
    @TmsLink("001")
    public void test01teacherLogin() {
        Teacher teacher = new Teacher("QA");
        teacherTest = new TeacherScenario();
        teacherTest.login(teacher);
    }


    @Test(groups = { "teacher", "register" }, description = "Test 02. Not possible to Login with empty fields.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("002")
    public void test02teacherLogin() {
        String personRole ="AS A TEACHER";
        String errorMsg=getProperty("error.enter.school.email");
        Teacher teacher = new Teacher("QA");
        teacher.setEmail("");
        teacher.setPassword("");
        teacherTest = new TeacherScenario();
        teacherTest.loginFailed(teacher,personRole,errorMsg );
    }

    @Test(groups = { "teacher", "register" }, description = "Test 03. Not possible to Login with empty field School email.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("003")
    public void test03teacherLogin() {
        String personRole ="AS A TEACHER";
        String errorMsg=getProperty("error.enter.school.email");
        Teacher teacher = new Teacher("QA");
        teacher.setEmail("");
        teacherTest = new TeacherScenario();
        teacherTest.loginFailed(teacher,personRole,errorMsg );
    }

    @Test(groups = { "teacher", "register" }, description = "Test 04. Not possible to Login with empty field Password.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("004")
    public void test04teacherLogin() {
        String personRole ="AS A TEACHER";
        String errorMsg=getProperty("error.enter.your.pass");
        Teacher teacher = new Teacher("QA");
        teacher.setPassword("");
        teacherTest = new TeacherScenario();
        teacherTest.loginFailed(teacher,personRole,errorMsg );
    }

    @Test(groups = { "teacher", "register" }, description = "Test 05. Login with  wrong field School email.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("005")
    public void test05teacherLogin() {
        String personRole ="AS A TEACHER";
        String errorMsg=getProperty("error.wrong.school");
        Teacher teacher = new Teacher("QA");
        teacher.setEmail("abc@d.e");
        teacherTest = new TeacherScenario();
        teacherTest.loginFailed(teacher,personRole,errorMsg );
    }

    @Test(groups = { "teacher", "register" }, description = "Test 06. Not possible to Login with empty field Password.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("006")
    public void test06teacherLogin() {
        String personRole ="AS A TEACHER";
        String errorMsg=getProperty("error.wrong.pass");
        Teacher teacher = new Teacher("QA");
        teacher.setPassword("qaz123cde111");
        teacherTest = new TeacherScenario();
        teacherTest.loginFailed(teacher,personRole,errorMsg );
    }

}