package com.im.scheduler;

public class UserForTeacher {
    public String assignment_change,dept, des,email,empty_room_dayPath,empty_room_timePath,exam_change,gen,name,phone, schedule_change;

    public UserForTeacher() {
    }

    public UserForTeacher(String assignment_change, String dept, String des, String email, String empty_room_dayPath, String empty_room_timePath, String exam_change, String gen, String name, String phone, String schedule_change) {
        this.assignment_change = assignment_change;
        this.dept = dept;
        this.des = des;
        this.email = email;
        this.empty_room_dayPath = empty_room_dayPath;
        this.empty_room_timePath = empty_room_timePath;
        this.exam_change = exam_change;
        this.gen = gen;
        this.name = name;
        this.phone = phone;
        this.schedule_change = schedule_change;
    }
}
