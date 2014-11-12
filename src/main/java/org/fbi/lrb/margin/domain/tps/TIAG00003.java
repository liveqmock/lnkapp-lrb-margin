package org.fbi.lrb.margin.domain.tps;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.fbi.lrb.margin.domain.tps.base.TpsMsg;
import org.fbi.lrb.margin.domain.tps.base.TpsMsgBody;

/**
 * 1、请求报文
     结点名称	中文名称	类型	长度	备注
     InstCode	入账帐号	C	20	入账帐号
     InstSeq	竞买号(唯一)	C	100	相当于订单号
     Count	到账日期	C	14	格式年月日（yyyyMMddHHmmss），中间无分隔，如：20060911112233
     到账日期第二天子账号将自动失效，但销户时间需要交易结束并保证金已退还
 */
@XStreamAlias("root")
public class TIAG00003 extends TpsMsg {
    private MsgBody body = new MsgBody();

    public MsgBody getBody() {
        return body;
    }

    public static class MsgBody extends TpsMsgBody {
        private String InstCode;
        private String InstSeq;
        private String MatuDay;

        public String getInstCode() {
            return InstCode;
        }

        public void setInstCode(String instCode) {
            this.InstCode = instCode;
        }

        public String getInstSeq() {
            return InstSeq;
        }

        public void setInstSeq(String instSeq) {
            this.InstSeq = instSeq;
        }

        public String getMatuDay() {
            return MatuDay;
        }

        public void setMatuDay(String matuDay) {
            MatuDay = matuDay;
        }

        @Override
        public String toString() {
            return "MsgBody{" +
                    "InstCode='" + InstCode + '\'' +
                    ", InstSeq='" + InstSeq + '\'' +
                    ", Count='" + MatuDay + '\'' +
                    '}';
        }
    }


    public static void main(String[] argv) {
        String xml =
                "<?xml version='1.0' encoding='GBK'?>" +
                        "<root>" +
                        "<head>" +
                        "<TransCode>G00003</TransCode>" +
                        "<TransDate>20060816</TransDate>" +
                        "<TransTime>174300</TransTime>" +
                        "<SeqNo>2005081600000001</SeqNo>" +
                        "</head>" +
                        "<body>" +
                        "<InstCode>111</InstCode>" +
                        "<InstSeq>222</InstSeq>" +
                        "<Count>333</Count>" +
                        "</body>" +
                        "</root>";

        TpsMsg tia = new TIAG00003();
        tia = tia.toBean(xml);
        System.out.println(">>>>" + tia.getHead());
        System.out.println(tia.getBody());

        System.out.println(">>>>" + tia.toXml(tia));
    }
}
