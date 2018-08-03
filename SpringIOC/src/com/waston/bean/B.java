package com.waston.bean;

/**
 * @author wangtao
 * @date 2017-2017/11/30-23:18
 **/
public class B {

    private String name;

    private A a;

    public B() {
        System.out.println("------B已初始化--------");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "B{" +
                "name='" + name + '\'' +
                ", a=" + a +
                '}';
    }
}
