package com.ngbj.wallpaper.bean.entityBean;

import java.io.Serializable;

/**
 *       基本上后台返回的数据格式如下：
 *       {
 *          "Success":true,
            "code":200,
            "msg":"成功",
            "Data":[]
            }
 或

         {
            "Success":true,
             "code":200,
             "msg":"成功",
             "Data":{}
            }

      T 泛型是在运行时动态的获取传递过来的参数
 */
public class HttpResponse<T> implements Serializable{
    private boolean success;
    private int code;
    private String message;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
