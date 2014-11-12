package org.fbi.lrb.margin.server.tcpserver;

/**
 * Created by zhanrui on 2014/11/6.
 */
public class TxnContext {
    private String msgtia;
    private String msgtoa;

    public String getMsgtia() {
        return msgtia;
    }

    public void setMsgtia(String msgtia) {
        this.msgtia = msgtia;
    }

    public String getMsgtoa() {
        return msgtoa;
    }

    public void setMsgtoa(String msgtoa) {
        this.msgtoa = msgtoa;
    }
}
