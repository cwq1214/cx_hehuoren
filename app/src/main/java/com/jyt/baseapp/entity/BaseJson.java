package com.jyt.baseapp.entity;

/**
 * Created by chenweiqi on 2017/6/6.
 */

public class BaseJson<T> {
    public T data;
    public boolean ret;
    public String forUser;
    public String forWorker;
    public boolean token;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public String getForUser() {
        return forUser;
    }

    public void setForUser(String forUser) {
        this.forUser = forUser;
    }

    public String getForWorker() {
        return forWorker;
    }

    public void setForWorker(String forWorker) {
        this.forWorker = forWorker;
    }
}
