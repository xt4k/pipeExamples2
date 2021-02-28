package app.pageobject;

import app.helper.BaseTestGui;
import app.helper.pojo.Student;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class RosterPageObject extends BasePageObject {
    private SelenideElement seRegisterStudents =$(By.xpath("//*[@id='registerStudents']/button"));
    private SelenideElement seStudentName =$(By.xpath("//*[@id='tableWrapperContent']//span[@data-tooltip]/a"));

    @Step("Register student.")
    public void register() {
        seRegisterStudents.click();
        infoShot(getMethodName());
    }

    @Step("Check registered student by Name (First and Last).")
    public void checkStudent(Student student) {
        seStudentName.shouldHave(exactText(student.getFirstName()+" "+student.getLastName()));
    }
}
