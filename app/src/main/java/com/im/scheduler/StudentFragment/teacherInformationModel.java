package com.im.scheduler.StudentFragment;

public class teacherInformationModel {
    String Email,Name,Phone,dept,des;

    public teacherInformationModel() {
    }

    public teacherInformationModel(String email, String name, String phone, String dept, String des) {
        Email = email;
        Name = name;
        Phone = phone;
        this.dept = dept;
        this.des = des;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}

