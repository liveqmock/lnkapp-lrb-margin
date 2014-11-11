package org.fbi.lrb.margin.domain.cbs.T1100Request;

import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsTia1100 {
    @DataField(seq = 1)
    private String acctNo;
    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    @Override
    public String toString() {
        return "CbsTia1100{" +
                ", acctNo='" + acctNo + '\'' +
                '}';
    }
}