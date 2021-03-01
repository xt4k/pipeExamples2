package app.pageobject;

import app.helper.pojo.Teacher;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class MyAccountPageObject extends BasePageObject {

    private SelenideElement seFname = $(By.ByXPath.id("UserNameFirst"));
    private SelenideElement seLname = $(By.ByXPath.id("UserNameLast"));

    private SelenideElement seSchool = $(By.ByXPath.id("school-district"));

    @Step("Teacher's data verification")
    public MyAccountPageObject checkTeacherInfo(Teacher teacher) {
        attachPageScreenShot(getMethodName());
        seFname.shouldHave(value(teacher.getFirstName()));
        seLname.shouldHave(value(teacher.getLastName()));
        seEmail.shouldHave(value(teacher.getEmail()));
        sePassword.shouldBe(visible)
                .shouldNotBe(empty);
        seSchool.shouldHave(text(teacher.getSchool()));
        return new MyAccountPageObject();
    }
}
