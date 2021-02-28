package app.tests.teacher;

import app.helper.BaseTestGui;
import app.helper.pojo.Student;
import app.helper.pojo.Teacher;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static app.pageobject.BasePageObject.reportInfo;

@Epic("MobyMax Automation.")
@Feature("Teacher")
@Story("Student register")

public class StudentRegisterTests extends BaseTestGui {
    private TeacherScenario teacherTest;


    @BeforeClass(description = "Pre-testset fixture.")
    public void preTestset() {
      }

    @BeforeMethod(description = "Pre-test fixture.")
    public void preTest() {

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
        infoShot(getMethodName());
    }

    @Test(groups = { "teacher", "register" }, description = "Test 01. Teacher register Student.")//enabled = false,)
    @TmsLink("019")
    @Severity(SeverityLevel.BLOCKER)  //  @TmsLink("4325")
    public void test01registerStudent() {
        Student student = new Student();
        Teacher teacher = new Teacher("QA");
        teacherTest = new TeacherScenario();
        teacherTest.registerStudent(teacher,student);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 02. Teacher register group of students.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("020")
    public void test02registerStudentGroup() {
        int number=10;
        Teacher teacher = new Teacher("QA");
        teacherTest = new TeacherScenario();
        teacherTest.registerStudent(teacher,number);
    }

}