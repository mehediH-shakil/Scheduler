package com.im.scheduler.StudentFragment;

public class classScheduleModel {
    String course,day,end,room,start,teacher;

    public classScheduleModel() {
    }

    public classScheduleModel(String course, String day, String end, String room, String start, String teacher) {
        this.course = course;
        this.day = day;
        this.end = end;
        this.room = room;
        this.start = start;
        this.teacher = teacher;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
