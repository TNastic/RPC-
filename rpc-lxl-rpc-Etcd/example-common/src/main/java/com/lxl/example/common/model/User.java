package com.lxl.example.common.model;

import java.io.Serializable;

/**
 * 实现序列化接口，为网络传输Java对象做准备
 */
public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
