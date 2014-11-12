package org.fbi.lrb.margin.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 第三方服务器 client
 * User: zhanrui
 * Date: 13-11-27
 */
public class TpsSocketClient {
    private String ip;
    private int port;
    private int timeout = 30000; //默认超时时间：ms  连接超时与读超时统一
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public TpsSocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * @throws Exception 其中：SocketTimeoutException为超时异常
     */
    public byte[] call(byte[] sendbuf) throws Exception {
        byte[] recvbuf = null;

        InetAddress addr = InetAddress.getByName(ip);
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(addr, port), timeout);
            socket.setSoTimeout(timeout);

            OutputStream os = socket.getOutputStream();
            os.write(sendbuf);
            os.flush();

            InputStream is = socket.getInputStream();
            recvbuf = new byte[6];
            int readNum = is.read(recvbuf);
            if (readNum == -1) {
                throw new RuntimeException("服务器连接已关闭!");
            }
            if (readNum < 6) {
                throw new RuntimeException("读取报文头长度部分错误...");
            }
            int msgLen = Integer.parseInt(new String(recvbuf).trim());
            //logger.info("报文体长度:" + msgLen);
            recvbuf = new byte[msgLen];

            //连接不稳定时 需延时一定时间
            Thread.sleep(100);

            readNum = is.read(recvbuf);   //阻塞读
            if (readNum != msgLen) {
                throw new RuntimeException("报文长度错误,报文头指示长度:[" + msgLen + "], 实际获取长度:[" + readNum + "]");
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                //
            }
        }
        return recvbuf;
    }


    public static void main(String... argv) throws UnsupportedEncodingException {
        TpsSocketClient mock = new TpsSocketClient("127.0.0.1", 2308);

        String msg = "..........";

        String strLen = null;
        strLen = "" + (msg.getBytes("GBK").length);
        String lpad = "";
        for (int i = 0; i < 6 - strLen.length(); i++) {
            lpad += "0";
        }
        strLen = lpad + strLen;


        byte[] recvbuf = new byte[0];
        try {
            recvbuf = mock.call((strLen + msg).getBytes("GBK"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.printf("服务器返回：%s\n", new String(recvbuf, "GBK"));
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
