package org.fbi.lrb.margin.domain.tps;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.fbi.lrb.margin.domain.tps.base.TpsMsg;
import org.fbi.lrb.margin.domain.tps.base.TpsMsgBody;

/**
 * 1��������
     �������	��������	����	����	��ע
     InstCode	�����ʺ�	C	20	�����ʺ�
     InstSeq	�����(Ψһ)	C	100	�൱�ڶ�����
     Count	��������	C	14	��ʽ�����գ�yyyyMMddHHmmss�����м��޷ָ����磺20060911112233
     �������ڵڶ������˺Ž��Զ�ʧЧ��������ʱ����Ҫ���׽�������֤�����˻�
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
