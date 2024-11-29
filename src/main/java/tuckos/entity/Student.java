package tuckos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Student")
public class Student {
    @Id
    private String rollNo;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String password;

    // getters
    public String getStudentName() {
        return studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getPassword() {
        return password;
    }

    // setters
    public void setStudentName(String name) {
        this.studentName = name;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}