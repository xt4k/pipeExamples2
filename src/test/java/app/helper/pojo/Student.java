package app.helper.pojo;

import app.helper.BaseTestGui;
import static io.qameta.allure.Allure.step;
import static java.lang.System.getProperty;
import static org.apache.commons.lang.RandomStringUtils.random;

public class Student extends BaseTestGui {
    private String index;
    private String firstName;
    private String lastName;
    private int grade;
    private String username;
    private String email;
    private String password;
    private String studentId;

    public String getStudentId() { return studentId; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGrade() {
        return grade;
    }

    public String getPassword() {
        return password;
    }


    public Student() {
        index = random(5, true, true);
        grade=1;
        firstName=getProperty("student.prefix")+"_FirstName_"+ index;
        lastName=getProperty("student.prefix")+"_LastName_"+ index;
        username =getProperty("student.prefix")+"_login_"+ index;
        password = getProperty("student.prefix")+"_pass_"+ index;
        studentId=getProperty("student.prefix")+"_Id_"+ index;
        step("student auto-generated.");
    }

    @Override
    public String toString() {
        return "Student{" +
                "index='" + index + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", grade=" + grade +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


