package org.fbi.lrb.margin.domain.tps;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.fbi.lrb.margin.domain.tps.base.TpsMsg;
import org.fbi.lrb.margin.domain.tps.base.TpsMsgBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Result	处理代码　	C　	2	00：成功处理； 09： 其它错误；99：系统错误
 * InMemo	收款账号	C	100	即子账号
 * Count	记录数	C	2
 * listEntity				List节点，数组描叙
 * <Entity rowNum="序号">				从0开始
 * InDate	到帐时间	C	14	20060911150101
 * InAmount	到帐金额	C		单位（元），2位小数点
 * InName	付款人户名	C	200
 * InAcct	付款人账号	C	100
 * InBankFLCode	银行交易流水号	C	64
 */
@XStreamAlias("root")
public class TOAG00005 extends TpsMsg {
    private MsgBody body = new MsgBody();

    public MsgBody getBody() {
        return body;
    }

    public static class MsgBody extends TpsMsgBody {
        private String Result;
        private String InMemo;
        private String Count;
        private List<Entity> listEntity = new ArrayList<Entity>();

        @XStreamAlias("Entity")
        public static class Entity {
            @XStreamAsAttribute
            public String rowNum = "";
            private String InDate;
            private String InAmount;
            private String InName;
            private String InAcct;
            private String InBankFLCode;

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

            public String getInBankFLCode() {
                return InBankFLCode;
            }

            public void setInBankFLCode(String inBankFLCode) {
                InBankFLCode = inBankFLCode;
            }

            @Override
            public String toString() {
                return "Entity{" +
                        "InDate='" + InDate + '\'' +
                        ", InAmount='" + InAmount + '\'' +
                        ", InName='" + InName + '\'' +
                        ", InAcct='" + InAcct + '\'' +
                        ", InBankFLCode='" + InBankFLCode + '\'' +
                        '}';
            }
        }

        //====
        public String getResult() {
            return Result;
        }

        public void setResult(String result) {
            this.Result = result;
        }

        public String getInMemo() {
            return InMemo;
        }

        public void setInMemo(String inMemo) {
            this.InMemo = inMemo;
        }

        public String getCount() {
            return Count;
        }

        public void setCount(String count) {
            Count = count;
        }

        public List<Entity> getListEntity() {
            return listEntity;
        }

        public void setListEntity(List<Entity> listEntity) {
            this.listEntity = listEntity;
        }

        @Override
        public String toString() {
            return "MsgBody{" +
                    "Result='" + Result + '\'' +
                    ", InMemo='" + InMemo + '\'' +
                    ", Count='" + Count + '\'' +
                    ", listEntity=" + listEntity +
                    '}';
        }
    }

    public static void main(String[] argv) {
        String xml = "<?xml version='1.0' encoding='GBK'?>\n" +
                "<root>\n" +
                "<head>\n" +
                "\t<TransCode>G00005</TransCode>\n" +
                "\t<TransDate>20060816</TransDate>\n" +
                "\t<TransTime>174300</TransTime>\n" +
                "\t<SeqNo>2005081600000001</SeqNo>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t\t<Result>00</Result>\n" +
                "\t\t<InMemo>12312313123345</InMemo>\n" +
                "<Count>2</Count>\n" +
                "<listEntity>\n" +
                "\t\t\t<Entity rowNum=\"0\">\n" +
                "\t\t\t\t<InDate>20100909110908</InDate>\n" +
                "\t\t\t\t<InAmount>120909.00</InAmount>\n" +
                "\t\t\t\t<InName>张三</InName>\n" +
                "\t\t\t\t<InAcct>53432432</InAcct>\n" +
                "<InBankFLCode>3243243244324</InBankFLCode>\n" +
                "\t\t\t</Entity>\n" +
                "\t<Entity rowNum=\"1\">\n" +
                "\t\t\t\t<InDate>20100910120909</InDate>\n" +
                "\t\t\t\t<InAmount>130909.00</InAmount>\n" +
                "\t\t\t\t<InName>张三</InName>\n" +
                "\t\t\t\t<InAcct>53432432</InAcct>\n" +
                "<InBankFLCode>3243243244325</InBankFLCode>\n" +
                "\t\t\t</Entity>\n" +
                "</listEntity>\n" +
                "</body>\n" +
                "</root>\n";

        TpsMsg msgBean = new TOAG00005();
        msgBean = msgBean.toBean(xml);
        System.out.println(">>>>" + msgBean.getHead());
        System.out.println(msgBean.getBody());

        System.out.println(">>>>" + msgBean.toXml(msgBean));
    }
}
