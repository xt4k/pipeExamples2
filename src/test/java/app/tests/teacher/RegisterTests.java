package app.tests.teacher;

import app.helper.BaseTestGui;
import app.helper.pojo.Teacher;
import app.pageobject.MyAccountPageObject;
import app.pageobject.menu.HeaderLinksWidget;
import app.pageobject.menu.HeaderSubMenuWidget;
import app.pageobject.menu.PopupMenuWidget;
import app.pageobject.menu.TopMenuWidget;
import io.qameta.allure.*;
import org.testng.ITestResult;
import org.testng.annotations.*;

import static app.pageobject.BasePageObject.reportInfo;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.open;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;

@Epic("Pipeline as a Code.")
@Feature("Ui Tests")
@Story("Several passed and 1 failed test")

public class RegisterTests extends BaseTestGui {
    private TeacherScenario teacherTest;

    @BeforeClass(description = "Pre-testset fixture.")
    public void preTestset() {
        reportInfo(getMethodName());
    }

    @BeforeMethod(description = "Pre-test fixture.")
    public void preTest() {
        reportInfo(getMethodName());
    }


    @Test(groups = { "teacher", "register" }, description = "Test 01.Register new Teacher .")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)  //  @TmsLink("4325")
    public void test01teacherRegister() {
        Teacher teacher = new Teacher();
        teacherTest = new TeacherScenario();
        teacherTest.register(teacher);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 02. Register new Teacher with empty form fields.", enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("007")
    public void test02teacherRegister() {
        Teacher teacher = new Teacher("","","","","");
        String errorMsg=getProperty("error.enter.first.name");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister2(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 03. Register new Teacher with empty field First Name.", enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("008")
    public void test03teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("");
        String errorMsg=getProperty("error.enter.first.name");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 04. Register new Teacher with empty field Last Name.",enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("009")
    public void test04teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setLastName("");
        String errorMsg=getProperty("error.enter.last.name");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 05. Register new Teacher with empty field School/Organization.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("010")
    public void test05teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setSchool("");
        String errorMsg=getProperty("error.select.school");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister2(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 06. Register new Teacher with empty field Email Address.", enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("011")
    public void test06teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setEmail("");
        String errorMsg=getProperty("error.enter.email");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 07. Register new Teacher with empty field Password.", enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("012")
    public void test07teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setPassword("");
        String errorMsg=getProperty("error.enter.pass");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 08. Register new Teacher with wrong field School/Organization.", enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("013")
    public void test08teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setSchool("");
        String errorMsg=getProperty("error.select.school");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister2(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 09. Register new Teacher with wrong field Email Address.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("014")
    public void test09teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setEmail("abc.de,gh");
        String errorMsg=getProperty("error.enter.email");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 10. Register new Teacher with wrong field Email Address." +
            " !!!MINOR ISSUE!!!",enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("15")
    @Issue("1")
    public void test10teacherRegister() {
        Teacher teacher = new Teacher();
        String rand =teacher.getIndex();
        String email= rand +"@"+ rand+"."+rand;//like "1234@1234.1234"
        teacher.setEmail(email);
        String errorMsg=getProperty("error.enter.email");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }


    @Test(groups = { "teacher", "register" }, description = "Test 11. Register new Teacher with field First Name: '    '."+
      " !!!MINOR ISSUE!!!")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("16")
    @Issue("2")
    public void test11teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("      ");
        String errorMsg=getProperty("error.enter.first.name");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 12. Register new Teacher field Last Name: '     '."+
            " !!!MINOR ISSUE!!!",enabled = false)
    @TmsLink("17")
    @Issue("2")
    @Severity(SeverityLevel.BLOCKER)
    public void test12teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setLastName("       ");
        String errorMsg=getProperty("error.enter.last.name");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

    @Test(groups = { "teacher", "register" }, description = "Test 13. Register new Teacher for busy email.")//enabled = false,)
    @Severity(SeverityLevel.BLOCKER)
    @TmsLink("18")
    public void test13teacherRegister() {
        Teacher teacher = new Teacher();
        teacher.setEmail(getProperty("aqa.teacher.email"));

        String errorMsg=getProperty("error.email.busy");
        teacherTest = new TeacherScenario();
        teacherTest.failedRegister(teacher,errorMsg);
    }

}