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
 * ������������ client
 * User: zhanrui
 * Date: 13-11-27
 */
public class TpsSocketClient {
    private String ip;
    private int port;
    private int timeout = 30000; //Ĭ�ϳ�ʱʱ�䣺ms  ���ӳ�ʱ�����ʱͳһ
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public TpsSocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * @throws Exception ���У�SocketTimeoutExceptionΪ��ʱ�쳣
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
                throw new RuntimeException("�����������ѹر�!");
            }
            if (readNum < 6) {
                throw new RuntimeException("��ȡ����ͷ���Ȳ��ִ���...");
            }
            int msgLen = Integer.parseInt(new String(recvbuf).trim());
            //logger.info("�����峤��:" + msgLen);
            recvbuf = new byte[msgLen];

            //���Ӳ��ȶ�ʱ ����ʱһ��ʱ��
            Thread.sleep(100);

            readNum = is.read(recvbuf);   //������
            if (readNum != msgLen) {
                throw new RuntimeException("���ĳ��ȴ���,����ͷָʾ����:[" + msgLen + "], ʵ�ʻ�ȡ����:[" + readNum + "]");
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

        System.out.printf("���������أ�%s\n", new String(recvbuf, "GBK"));
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
