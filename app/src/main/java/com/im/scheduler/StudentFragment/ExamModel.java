package com.im.scheduler.StudentFragment;

public class ExamModel {
    String sec,sem,teacher,time,title;

    public ExamModel() {

    }

    public ExamModel(String sec, String sem, String teacher, String time, String title) {
        this.sec = sec;
        this.sem = sem;
        this.teacher = teacher;
        this.time = time;
        this.title = title;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
