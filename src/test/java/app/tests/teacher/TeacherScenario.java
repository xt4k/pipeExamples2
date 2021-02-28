package app.tests.teacher;


import app.helper.BaseTestGui;
import app.helper.pojo.Student;
import app.helper.pojo.Teacher;
import app.pageobject.*;
import app.pageobject.form.RegisterFormWidget;
import app.pageobject.form.SignInForm;
import app.pageobject.form.StudentRegisterGridWidget;
import app.pageobject.menu.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;

import static app.pageobject.BasePageObject.reportInfo;
import static com.codeborne.selenide.Selenide.refresh;


@Epic("MobyMax Automation.")
@Feature("Teacher")
@Story("Common functionality")
public class TeacherScenario extends BaseTestGui {

    @Step("Automated test: Teacher registration.")
    void register(Teacher teacher) {
        refresh();
        infoShot("STARTED AUTO-TEST.");
        new RegisterFormWidget().setField("First Name", teacher.getFirstName())
                .setField("Last Name", teacher.getLastName())
                //.setState(teacher.getState())
                .selectSchool(teacher.getSchool())
                .setField("Email Address", teacher.getEmail())
                .setField("Create a Password", teacher.getPassword())
                .register();
        new TopMenuWidget().selectMenuItem("More");
        new PopupMenuWidget().selectMenuItem("My Account");
        new MyAccountPageObject().checkTeacherInfo(teacher);
        new HeaderLinksWidget().selectLink("...");
        new HeaderSubMenuWidget().selectLink("Log Out")
                .toMainPage();
        infoShot("COMPLETED AUTO-TEST.");
    }

    @Step("Automated test: Teacher Login.")
    void login(Teacher teacher) {
        refresh();
        infoShot("STARTED AUTO-TEST.");
        new TopLinksMenuWidget().signIn()
                .as("AS A TEACHER")
                .setEmail(teacher.getEmail())
                .setPass(teacher.getPassword())
                .signOn();
        new TopMenuWidget().selectMenuItem("More");
        new PopupMenuWidget().selectMenuItem("My Account");
        new MyAccountPageObject().checkTeacherInfo(teacher);
        new HeaderLinksWidget().selectLink("...");
        new HeaderSubMenuWidget().selectLink("Log Out")
                .toMainPage();
        infoShot("COMPLETED AUTO-TEST.");

    }

    @Step("Automated test: {1}-student group registering.")
    void registerStudent(Teacher teacher, int number) {
        refresh();
        infoShot("STARTED AUTO-TEST.");
        new TopLinksMenuWidget().signIn()
                .as("AS A TEACHER")
                .setEmail(teacher.getEmail())
                .setPass(teacher.getPassword())
                .signOn();
        new LeftMainMenuWidget().selectMenuItem("Roster");
        boolean studentExist = new StudentRegisterGridWidget().isAnyStudentPresent();
        reportInfo("Exist student:" + studentExist);
        if (studentExist) {
            new StudentRegisterGridWidget().select()
                    .doStudents("DELETE");
            new ConfirmationPopUpWidget().select("DELETE");
        }
        new RosterPageObject().register();
        for (int i = 1; i <= number; i++) {
            Student student = new Student();
            new StudentRegisterGridWidget().submit(student, i);
        }
        new StudentRegisterGridWidget().doAction("FINISH");
        // new RosterPageObject().checkStudent(student);
        new TopMenuWidget().selectMenuItem("More");
        new PopupMenuWidget().selectMenuItem("Log Out");
        new SignInForm().toMainPage();
        infoShot("COMPLETED AUTO-TEST.");
    }

    @Step("Automated test: Failed Person Login.")
    void loginFailed(Teacher teacher, String personRole, String errorMsg) {
        refresh();
        infoShot("STARTED AUTO-TEST.");
        new TopLinksMenuWidget().signIn()
                .as(personRole)
                .setEmail(teacher.getEmail())
                .setPass(teacher.getPassword())
                .signOn();
        new SignInForm().checkError(errorMsg);
        new SignInForm().toMainPage();
        infoShot("COMPLETED AUTO-TEST.");
    }

    @Step("Automated test: Failed teacher registration.")
    void failedRegister(Teacher teacher, String errorMsg) {
        refresh();
        infoShot("STARTED AUTO-TEST.");
        new RegisterFormWidget().setField("First Name", teacher.getFirstName())
                .setField("Last Name", teacher.getLastName())
                //.setState(teacher.getState())
                .selectSchool(teacher.getSchool())
                .setField("Email Address", teacher.getEmail())
                .setField("Create a Password", teacher.getPassword())
                .register();
        new BasePageObject().checkError(errorMsg);
        infoShot("COMPLETED AUTO-TEST.");
    }


    @Step("Automated test: Failed teacher registration.")
    void failedRegister2(Teacher teacher, String errorMsg) {
        refresh();
        infoShot("STARTED AUTO-TEST.");
        new RegisterFormWidget().setField("First Name", teacher.getFirstName())
                .setField("Last Name", teacher.getLastName())
                //.setState(teacher.getState())
                .setSchool(teacher.getSchool())
                .setField("Email Address", teacher.getEmail())
                .setField("Create a Password", teacher.getPassword())
                .register();
        new BasePageObject().checkError(errorMsg);
        infoShot("COMPLETED AUTO-TEST.");
    }

    @Step("Automated test: Student registering.")
    void registerStudent(Teacher teacher, Student student) {
        refresh();
        infoShot("STARTED AUTO-TEST.");
        new TopLinksMenuWidget().signIn()
                .as("AS A TEACHER")
                .setEmail(teacher.getEmail())
                .setPass(teacher.getPassword())
                .signOn();
        new LeftMainMenuWidget().selectMenuItem("Roster");
        boolean studentExist = new StudentRegisterGridWidget().isAnyStudentPresent();
        reportInfo("Exist student:" + studentExist);
        if (studentExist) {
            new StudentRegisterGridWidget().select()
                    .doStudents("DELETE");
            new ConfirmationPopUpWidget().select("DELETE");
        }
        new RosterPageObject().register();
        new StudentRegisterGridWidget().submit(student)
                .doAction("FINISH");
        new RosterPageObject().checkStudent(student);

        new TopMenuWidget().selectMenuItem("More");
        new PopupMenuWidget().selectMenuItem("Log Out");
        new SignInForm().toMainPage();
        infoShot("COMPLETED AUTO-TEST.");
    }


}