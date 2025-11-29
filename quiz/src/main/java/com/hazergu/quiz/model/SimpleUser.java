package com.hazergu.quiz.model;

public class SimpleUser {
    private String name;
    private Integer age;

    public String getName(){
        return name;
    }

    public Integer getAge(){
        return age;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAge(Integer age){
        this.age = age;
    }

    @Override
    public String toString(){
        return "SimpleUser ["+" name='" + name + '\''+ ", age=" + age + ']';
    }
}
