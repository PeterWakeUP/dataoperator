package com.example.dataoperator.dto;

public class R {

    private Integer code;
    private String msg;


    private R(){

    }

    private R(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static R ok(){
        R r = new R(200, "success");
        return r;
    }

    public static R error(){
        R r = new R(-1, "error");
        return r;
    }
}
