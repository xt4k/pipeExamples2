package app.helper.pojo;

import app.helper.BaseTestGui;

import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static java.lang.System.getProperty;
import static org.apache.commons.lang.RandomStringUtils.random;

public class Teacher extends BaseTestGui {
    private String index;
    private String type;
    private String position;

    private String firstName;
    private String lastName;
    private String state;
    private String school;

    private String email;
    private String password;


    public Teacher() {
        index = random(5, true, true);
        type=getProperty("teacher.type");
        position=getProperty("teacher.position");
        firstName=getProperty("teacher.prefix")+"_FirstName_"+ index;
        lastName=getProperty("teacher.prefix")+"_LastName_"+ index;
        state=getProperty("teacher.state");
        school=getProperty("teacher.school");
        email="email_"+index+getProperty("teacher.email.domen");
        password = index;
        step("teacher auto-generated.");
    }

    public Teacher(String flag) {
        type=getProperty("teacher.type");
        position=getProperty("teacher.position");
        firstName=getProperty("aqa.teacher.first");
        lastName=getProperty("aqa.teacher.last");
        state="None";
        school=getProperty("aqa.teacher.school");
        email=getProperty("aqa.teacher.email");
        password = getProperty("aqa.teacher.pass");
        step(format("predefined %s teacher generated.",flag));
    }


    public Teacher(String fName,String lName,String schl,String mail,String psswd) {
        index = random(9, true, true);
        type=getProperty("teacher.type");
        position=getProperty("teacher.position");
        firstName=fName;
        lastName=lName;
        state=getProperty("teacher.state");
        school=schl;
        email=mail;
        password = psswd;
        step("teacher auto-generated.");
    }



    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
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

    @Override
    public String toString() {
        return "Teacher{" +
                ", index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", position='" + position + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", state='" + state + '\'' +
                ", school='" + school + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


