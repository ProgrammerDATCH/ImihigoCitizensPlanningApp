package com.imihigocizitensplanning.app.Models;

public class Notifikation {
    String msg;
    boolean done;

    public Notifikation() {
    }

    public Notifikation(String msg, boolean done) {
        this.msg = msg;
        this.done = done;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
