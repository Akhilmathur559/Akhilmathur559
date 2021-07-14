package com.white.testapp1;

public class dataHolder {
    String name,age,mobile;
    public dataHolder(String name,String age,String mobile){
        this.name=name;
        this.age=age;
        this.mobile=mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}