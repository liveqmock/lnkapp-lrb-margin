package org.fbi.lrb.margin.server.tcpserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * User: zhanrui
 * Date: 2014-11-06
 */
public class MessageServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);
    private static Map<String,Object> contextsMap = new ConcurrentHashMap<String,Object>();

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String reqXml)  {
        try {
            logger.info("服务器收到报文：" + reqXml);

            int startIndex = reqXml.indexOf("<TransCode>")+"<TransCode>".length();
            int endIndex = reqXml.indexOf("</TransCode>");
            String txnCode = reqXml.substring(startIndex, endIndex).trim().toUpperCase();

            TxnContext txnContext = new TxnContext();
            txnContext.setMsgtia(reqXml);
            Class clazz = Class.forName("org.fbi.lrb.margin.server.processor." + txnCode + "Processor");
            Processor processor = (Processor)clazz.newInstance();
            processor.service(txnContext);

            String respXml = txnContext.getMsgtoa();
            respXml = "<?xml version='1.0' encoding='GBK'?>" + respXml;
            String strLen = String.format("%06d", respXml.getBytes("GBK").length);

            ctx.writeAndFlush(strLen + respXml);
        } catch (ClassNotFoundException e) {
            logger.error("Processor not found!", e);
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Processor instance error!", e);
        } catch (Exception e){
            logger.error("Txn process error", e);
            //TODO 返回异常报文？
        }
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ChannelInactived.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }

}
