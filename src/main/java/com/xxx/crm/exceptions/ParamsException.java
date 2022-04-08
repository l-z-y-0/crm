package com.xxx.crm.exceptions;

public class ParamsException extends RuntimeException{
    private Integer code=300;
    private String msg="参数异常";

    public ParamsException(){
        super("参数异常");
    }

    public ParamsException(Integer code){
        super("参数异常");
        this.code=code;
    }
    public ParamsException(String msg){
        super(msg);
        this.msg=msg;
    }

    public ParamsException(Integer code,String msg){
        super(msg);
        this.code=code;
        this.msg=msg;
    }


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
