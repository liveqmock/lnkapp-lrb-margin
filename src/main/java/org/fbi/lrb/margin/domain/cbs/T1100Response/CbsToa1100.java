package org.fbi.lrb.margin.domain.cbs.T1100Response;

import org.fbi.linking.codec.dataformat.annotation.DataField;
import org.fbi.linking.codec.dataformat.annotation.SeperatedTextMessage;

/**
 * Created by zhanrui on 14-10-20.
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class CbsToa1100 {
    @DataField(seq = 1)
    private String instCode;
    @DataField(seq = 2)
    private String instSeq;
    @DataField(seq = 3)
    private String matuDay;

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public String getInstSeq() {
        return instSeq;
    }

    public void setInstSeq(String instSeq) {
        this.instSeq = instSeq;
    }

    public String getMatuDay() {
        return matuDay;
    }

    public void setMatuDay(String matuDay) {
        this.matuDay = matuDay;
    }

    @Override
    public String toString() {
        return "CbsToa1100{" +
                "instCode='" + instCode + '\'' +
                ", instSeq='" + instSeq + '\'' +
                ", matuDay='" + matuDay + '\'' +
                '}';
    }
}
