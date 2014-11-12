package org.fbi.lrb.margin.domain.tps.base;

import java.io.Serializable;

public class TpsMsgHead implements Serializable {
    public String TransCode = "";
    public String TransDate = "";
    public String TransTime = "";
    public String SeqNo = "";

    public String getTransCode() {
        return TransCode;
    }

    public void setTransCode(String transCode) {
        TransCode = transCode;
    }

    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String transDate) {
        TransDate = transDate;
    }

    public String getTransTime() {
        return TransTime;
    }

    public void setTransTime(String transTime) {
        TransTime = transTime;
    }

    public String getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(String seqNo) {
        SeqNo = seqNo;
    }

    @Override
    public String toString() {
        return "TpsMsgHead{" +
                "TransCode='" + TransCode + '\'' +
                ", TransDate='" + TransDate + '\'' +
                ", TransTime='" + TransTime + '\'' +
                ", SeqNo='" + SeqNo + '\'' +
                '}';
    }
}
