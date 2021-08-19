package com.dingshaoshuai.studyapt;

public class Test {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void lecture(String subject) {
        System.out.println("我要开始讲" + subject + "课了");
    }
}
