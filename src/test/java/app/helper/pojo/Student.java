package app.helper.pojo;

import app.helper.BaseTestGui;
import app.utils.CommonOperations;

import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static java.lang.System.getProperty;

public class Student extends BaseTestGui {
    private String index;

    private String firstName;
    private String lastName;

    private int grade;
    private String username;

    private String email;
    private String password;

    private String studentId;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    public Student() {
        CommonOperations co = new CommonOperations();
        index = co.getRandomMultiDigitNumber(5);

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


