package com.s11web.util;

/**
 * @author yongbw
 * @Description pack data into Json Object
 * @input <T> any data structure object
 * @output JsonResult Object
 */
public class JsonResult<T> {
    private boolean success;
    private String message;
    private T data;

    public JsonResult() {
        super();
    }

    public JsonResult(boolean success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public JsonResult(boolean success, String message, T data) {
        super();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public JsonResult(String message, T data) {
        super();
        if (data.equals(null)) {
            this.success = false;
        } else {
            this.success = true;
        }
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
