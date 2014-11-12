package org.fbi.lrb.margin.domain.tps;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.fbi.lrb.margin.domain.tps.base.TpsMsg;
import org.fbi.lrb.margin.domain.tps.base.TpsMsgBody;

/**
 InstCode	»Î’À’ ∫≈	C	100	»Î’À’ ∫≈
 InMemo	 ’øÓ’À∫≈	C	100	º¥◊”’À∫≈
 */
@XStreamAlias("root")
public class TIAG00005 extends TpsMsg {
    private MsgBody body = new MsgBody();

    public MsgBody getBody() {
        return body;
    }

    public static class MsgBody extends TpsMsgBody {
        private String InstCode;
        private String InMemo;

        public String getInstCode() {
            return InstCode;
        }

        public void setInstCode(String instCode) {
            this.InstCode = instCode;
        }

        public String getInMemo() {
            return InMemo;
        }

        public void setInMemo(String inMemo) {
            this.InMemo = inMemo;
        }

        @Override
        public String toString() {
            return "MsgBody{" +
                    "InstCode='" + InstCode + '\'' +
                    ", InMemo='" + InMemo + '\'' +
                    '}';
        }
    }
}
