package com.im.scheduler;

public class UserForStudent {
    public String Email, ID, Name, Phone, sem, sec, dept, gen, sechedule_change;
    public UserForStudent(){
    }

    public UserForStudent(String email, String ID, String name, String phone, String sem, String sec, String dept, String gen, String sechedule_change) {
        Email = email;
        this.ID = ID;
        Name = name;
        Phone = phone;
        this.sem = sem;
        this.sec = sec;
        this.dept = dept;
        this.gen = gen;
        this.sechedule_change = sechedule_change;
    }
}
