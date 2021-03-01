package app.pageobject.form;

import app.helper.pojo.Student;
import app.pageobject.BasePageObject;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static app.helper.BaseTestGui.getMethodName;
import static app.helper.BaseTestGui.infoShot;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.lang.String.valueOf;

public class StudentRegisterGridWidget extends BasePageObject {

    private SelenideElement seHeaderRow = $(By.xpath(".//*[@id='root']//div[@class='index--data_row___2tmRz  index--subTitledHeaderRow___-Ci94']"));
    private SelenideElement seFirstRow = $(By.xpath(".//*[@id='root']//div[@class='index--data_row___2tmRz  index--show_placeholders___2psWw ']"));
    private SelenideElement seCheckBoxAllStudents = $(By.xpath(".//*[@id='tableRowHeader']//input"));////*[@id="tableWrapperContent"]//div[@class="index--tableEmptyMessage___3tX-A"]
    private ElementsCollection ecButton = $$(By.xpath("//*[@id='tableFilterButtons']//button"));
    private ElementsCollection ecGridButtons = $$(By.xpath(".//*[@id='root']//button"));

    @Step("Submit student to first datagrid row")
    public StudentRegisterGridWidget submit(Student student) {

        infoShot(getMethodName());
        se = seFirstRow.lastChild();
        setTextToGridCell(se, student.getStudentId());
        setTextToGridCell(se.preceding(0), student.getPassword());
        setTextToGridCell(se.preceding(1), student.getStudentId());
        setTextToGridCell(se.preceding(2), valueOf(student.getGrade()));
        setTextToGridCell(se.preceding(3), student.getLastName());
        setTextToGridCell(se.preceding(4), student.getFirstName());

        attachPageScreenShot(getMethodName() + 99);
        return new StudentRegisterGridWidget();
    }

    @Step("Submit student to first datagrid row")
    public StudentRegisterGridWidget submit(Student student, int row) {
        SelenideElement seGridRow =seHeaderRow.sibling(row-1);
        attachPageScreenShot(getMethodName());
        se = seGridRow.lastChild();
        setTextToGridCell(se, student.getStudentId());
        setTextToGridCell(se.preceding(0), student.getPassword());
        setTextToGridCell(se.preceding(1), student.getStudentId());
        setTextToGridCell(se.preceding(2), valueOf(student.getGrade()));
        setTextToGridCell(se.preceding(3), student.getLastName());
        setTextToGridCell(se.preceding(4), student.getFirstName());

        attachPageScreenShot(getMethodName() + row+" done");
        return new StudentRegisterGridWidget();
    }

    @Step("Return true if any student is present in grid")
    public boolean isAnyStudentPresent() {
        boolean result = false;
        waitForLoaded();
        Selenide.sleep(5000);
        if (seCheckBoxAllStudents.exists()) result = true;

        attachPageScreenShot(getMethodName());
        return result;
    }

    @Step("Select All Students")
    public StudentRegisterGridWidget select() {
        seCheckBoxAllStudents.click();
        attachPageScreenShot(getMethodName());
        return new StudentRegisterGridWidget();
    }

    @Step("Operations with selected students list")
    public void doStudents(String operation) {
        se = ecButton.findBy(text(operation));
        attachPageScreenShot(getMethodName());
        se.click();
    }

    @Step("Operations with selected students list")
    public StudentRegisterGridWidget doAction(String action) {
        attachPageScreenShot(getMethodName());
        se = ecGridButtons.findBy(text(action));
        se.click();
        attachPageScreenShot(getMethodName());
        return new StudentRegisterGridWidget();

    }


}
