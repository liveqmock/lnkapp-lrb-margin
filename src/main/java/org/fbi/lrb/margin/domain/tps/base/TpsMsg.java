package org.fbi.lrb.margin.domain.tps.base;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

public  class TpsMsg implements Serializable {
    private TpsMsgHead head = new TpsMsgHead();
    //private TpsMsgBody body = new TpsMsgBody();

    public TpsMsg toBean(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(this.getClass());
        return (TpsMsg) xs.fromXML(xml);
    }
    public String toXml(TpsMsg bean) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(this.getClass());
        return  xs.toXML(bean);
    }

    //=======
    public TpsMsgHead getHead() {
        return head;
    }

    public void setHead(TpsMsgHead head) {
        this.head = head;
    }

    public TpsMsgBody getBody() {
        return null;
    }

}
