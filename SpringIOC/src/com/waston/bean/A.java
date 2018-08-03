package com.waston.bean;

/**
 * @author wangtao
 * @date 2017-2017/11/30-23:15
 **/
public class A {

    private String name;

    private int age;

    public A() {
        System.out.println("------A已初始化------");
    }

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

    @Override
    public String toString() {
        return "A{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
