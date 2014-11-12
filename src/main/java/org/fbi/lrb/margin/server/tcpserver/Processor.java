package org.fbi.lrb.margin.server.tcpserver;

/**
 * Created by zhanrui on 2014/11/6.
 */
public interface Processor {
    public void service(TxnContext ctx);
}
