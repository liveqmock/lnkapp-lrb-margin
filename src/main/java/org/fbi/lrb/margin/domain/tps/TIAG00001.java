package org.fbi.lrb.margin.domain.tps;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.fbi.lrb.margin.domain.tps.base.TpsMsg;
import org.fbi.lrb.margin.domain.tps.base.TpsMsgBody;

/**
 * 1、请求报文
     InstCode	入账帐号	C	64	入账帐号
     InDate	保证金到帐时间	C	14	20060911150101
     InAmount	到帐金额	C		单位（元），2位小数点
     InName	付款人户名	C	200
     InAcct	付款人账号	C	100
     InMemo	子账号	C	100	即子账号
     InBankFLCode	银行交易流水号	C	64	银行交易流水号,用于标示唯一一笔到账。
 */
@XStreamAlias("root")
public class TIAG00001 extends TpsMsg {
    private MsgBody body = new MsgBody();

    public MsgBody getBody() {
        return body;
    }

    public static class MsgBody extends TpsMsgBody {
        private String InstCode;
        private String InDate;
        private String InAmount;
        private String InName;
        private String InAcct;
        private String InMemo;
        private String InBankFLCode;

        public String getInstCode() {
            return InstCode;
        }

        public void setInstCode(String instCode) {
            InstCode = instCode;
        }

        public String getInDate() {
            return InDate;
        }

        public void setInDate(String inDate) {
            InDate = inDate;
        }

        public String getInAmount() {
            return InAmount;
        }

        public void setInAmount(String inAmount) {
            InAmount = inAmount;
        }

        public String getInName() {
            return InName;
        }

        public void setInName(String inName) {
            InName = inName;
        }

        public String getInAcct() {
            return InAcct;
        }

        public void setInAcct(String inAcct) {
            InAcct = inAcct;
        }

        public String getInMemo() {
            return InMemo;
        }

        public void setInMemo(String inMemo) {
            InMemo = inMemo;
        }

        public String getInBankFLCode() {
            return InBankFLCode;
        }

        public void setInBankFLCode(String inBankFLCode) {
            InBankFLCode = inBankFLCode;
        }

        @Override
        public String toString() {
            return "MsgBody{" +
                    "InstCode='" + InstCode + '\'' +
                    ", InDate='" + InDate + '\'' +
                    ", InAmount='" + InAmount + '\'' +
                    ", InName='" + InName + '\'' +
                    ", InAcct='" + InAcct + '\'' +
                    ", InMemo='" + InMemo + '\'' +
                    ", InBankFLCode='" + InBankFLCode + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TIAG00001{" +
                "body=" + body +
                '}';
    }
}
