package com.zk.timerpicker.gson;

import java.util.List;

public class GsonTempClassTwo {
    int code;
    List<Integer> data;
    String msg;

    public int getCode() {
        return code;
    }

    public GsonTempClassTwo setCode(int code) {
        this.code = code;
        return this;
    }

    public List<Integer> getData() {
        return data;
    }

    public GsonTempClassTwo setData(List<Integer> data) {
        this.data = data;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public GsonTempClassTwo setMsg(String msg) {
        this.msg = msg;
        return this;
    }


}
