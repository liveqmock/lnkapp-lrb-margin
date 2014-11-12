package org.fbi.lrb.margin.domain.tps;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.fbi.lrb.margin.domain.tps.base.TpsMsg;
import org.fbi.lrb.margin.domain.tps.base.TpsMsgBody;

/**
 * 结点名称	中文名称	类型	长度	备注
     Result	处理代码　	C	2	00：成功处理；01：接收重复；09： 其它错误；99：系统错误
     AcctNo	子账号	C	100	子账号由银行核心系统自动生成
     Count	处理结果	C	500
 */
@XStreamAlias("root")
public class TOAG00001 extends TpsMsg {
    private MsgBody body = new MsgBody();

    public MsgBody getBody() {
        return body;
    }

    public static class MsgBody extends TpsMsgBody {
        private String Result;
        private String AcctNo;
        private String AddWord;

        public String getResult() {
            return Result;
        }

        public void setResult(String result) {
            this.Result = result;
        }

        public String getAcctNo() {
            return AcctNo;
        }

        public void setAcctNo(String acctNo) {
            this.AcctNo = acctNo;
        }

        public String getAddWord() {
            return AddWord;
        }

        public void setAddWord(String addWord) {
            AddWord = addWord;
        }

        @Override
        public String toString() {
            return "MsgBody{" +
                    "Result='" + Result + '\'' +
                    ", AcctNo='" + AcctNo + '\'' +
                    ", Count='" + AddWord + '\'' +
                    '}';
        }
    }

}
