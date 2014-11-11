package org.fbi.lrb.margin.domain.cbs.T1200Request;

import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

import java.math.BigDecimal;

@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia1200 {
    @DataField(seq = 1)
    private String instCode;
    @DataField(seq = 2)
    private String inData;
    @DataField(seq = 3)
    private BigDecimal inAmount;
    @DataField(seq = 4)
    private String inName;
    @DataField(seq = 5)
    private String inAcct;
    @DataField(seq = 6)
    private String inMemo; //×ÓÕÊºÅ

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public String getInData() {
        return inData;
    }

    public void setInData(String inData) {
        this.inData = inData;
    }

    public BigDecimal getInAmount() {
        return inAmount;
    }

    public void setInAmount(BigDecimal inAmount) {
        this.inAmount = inAmount;
    }

    public String getInName() {
        return inName;
    }

    public void setInName(String inName) {
        this.inName = inName;
    }

    public String getInAcct() {
        return inAcct;
    }

    public void setInAcct(String inAcct) {
        this.inAcct = inAcct;
    }

    public String getInMemo() {
        return inMemo;
    }

    public void setInMemo(String inMemo) {
        this.inMemo = inMemo;
    }

    @Override
    public String toString() {
        return "CbsTia1200{" +
                "instCode='" + instCode + '\'' +
                ", inData='" + inData + '\'' +
                ", inAmount=" + inAmount +
                ", inName='" + inName + '\'' +
                ", inAcct='" + inAcct + '\'' +
                ", inMemo='" + inMemo + '\'' +
                '}';
    }
}