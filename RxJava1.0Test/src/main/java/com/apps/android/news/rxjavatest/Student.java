package com.apps.android.news.rxjavatest;

import java.util.List;

/**
 * Created by android on 2017/3/8.
 */

public class Student {
    private String name;
    private List<Course> courses;

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public void setName(String name) {
        this.name = name;
    }

}
