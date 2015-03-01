package org.fbi.lrb.margin.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.fbi.lrb.margin.domain.cbs.T1200Request.CbsTia1200;
import org.fbi.lrb.margin.domain.tps.TIAG00001;
import org.fbi.lrb.margin.domain.tps.TOAG00001;
import org.fbi.lrb.margin.enums.TxnRtnCode;
import org.fbi.lrb.margin.helper.MybatisFactory;
import org.fbi.lrb.margin.helper.ProjectConfigManager;
import org.fbi.lrb.margin.helper.TpsSocketClient;
import org.fbi.lrb.margin.internal.AppActivator;
import org.fbi.lrb.margin.repository.dao.LrbMargTxnMapper;
import org.fbi.lrb.margin.repository.model.LrbMargTxn;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by zhanrui on 2014-11-11.
 * 1581200 记账
 */
public class T1200Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        String txnDate = request.getHeader("txnTime").substring(0,8);
        String hostTxnsn = request.getHeader("serialNo");

        CbsTia1200 tia = new CbsTia1200();
        try {
            SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
            tia = (CbsTia1200) dataFormat.fromMessage(new String(request.getRequestBody(), "GBK"), "CbsTia1200");
        } catch (Exception e) {
            logger.error("[sn=" + hostTxnsn + "] " + "特色业务平台请求报文解析错误.", e);
            throw new RuntimeException(e);
        }


        //业务逻辑处理
        CbsRtnInfo cbsRtnInfo = null;
        try {
            cbsRtnInfo = processTxn(tia, txnDate,hostTxnsn);
            //特色平台响应
            response.setHeader("rtnCode", cbsRtnInfo.getRtnCode().getCode());
            String cbsRespMsg = cbsRtnInfo.getRtnMsg();

            response.setResponseBody(cbsRespMsg.getBytes(response.getCharacterEncoding()));
        } catch (Exception e) {
            logger.error("[sn=" + hostTxnsn + "] " + "交易处理异常.", e);
            throw new RuntimeException("交易处理异常", e);
        }

    }


    private CbsRtnInfo processTxn(CbsTia1200 tia, String txnDate,  String hostTxnsn) throws Exception {
        CbsRtnInfo cbsRtnInfo = new CbsRtnInfo();
        SqlSessionFactory sqlSessionFactory = null;
        SqlSession session = null;
        try {
            sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
            session = sqlSessionFactory.openSession();
            session.getConnection().setAutoCommit(false);
            LrbMargTxnMapper mapper = session.getMapper(LrbMargTxnMapper.class);

            LrbMargTxn txn = mapper.selectByPrimaryKey(txnDate + hostTxnsn);
            if (txn != null) {
                cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_FAILED);
                cbsRtnInfo.setRtnMsg("流水号重复");
                return cbsRtnInfo;
            }

            txn = new LrbMargTxn();
            txn.setInstcode(tia.getInstCode());
            txn.setInbankflcode(txnDate + hostTxnsn);
            txn.setInacct(tia.getInAcct());
            txn.setInname(tia.getInName());
            txn.setInamount(tia.getInAmount());
            txn.setIndate(tia.getInDate());  //14位日期
//            txn.setIndate(tia.getInDate() + new SimpleDateFormat("HHmmss").format(new Date()));  //14位日期
            txn.setInmemo(tia.getInMemo());  //子帐号

            mapper.insert(txn);

            //国土局端处理
            TIAG00001 tpsTia = new TIAG00001();
            String tpsTxnCode = "G00001";
            tpsTia.getHead().setTransCode(tpsTxnCode);
            tpsTia.getHead().setTransDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            tpsTia.getHead().setTransTime(new SimpleDateFormat("HHmmss").format(new Date()));
            //tpsTia.getHead().setSeqNo(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));  //默认值
            tpsTia.getHead().setSeqNo(txnDate + hostTxnsn);  //交易日期+主机交易流水号

            //设置报文体 20141204 linyong
            tpsTia.getBody().setInstCode(tia.getInstCode());
            tpsTia.getBody().setInDate(tia.getInDate());
//            tpsTia.getBody().setInDate(tia.getInDate() + new SimpleDateFormat("HHmmss").format(new Date()));
            tpsTia.getBody().setInAmount(tia.getInAmount().toString());
            tpsTia.getBody().setInName(tia.getInName());
            tpsTia.getBody().setInAcct(tia.getInAcct());
            tpsTia.getBody().setInMemo(tia.getInMemo());
            tpsTia.getBody().setInBankFLCode(txnDate + hostTxnsn);
            String reqXml = tpsTia.toXml(tpsTia);
            reqXml = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n"+reqXml;
            reqXml = StringUtils.replace(reqXml,"\r","");
            reqXml = StringUtils.replace(reqXml,"\n","");
            int intLength = reqXml.getBytes("GBK").length;
            String strLength = "";
            strLength = StringUtils.leftPad(intLength + "", 6, "0");
            reqXml = strLength +""+ reqXml;
            String respXml = new String(processThirdPartyServer(reqXml.getBytes("GBK"), tpsTxnCode), "GBK");
            TOAG00001 tpsToa = (TOAG00001)new TOAG00001().toBean(respXml);
            String resultCode = tpsToa.getBody().getResult();
            logger.info("结果码："+resultCode);

            if ("00".equals(resultCode)) { //交易处理成功
                session.commit();
                cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_SECCESS);
                cbsRtnInfo.setRtnMsg(TxnRtnCode.TXN_EXECUTE_SECCESS.getTitle());
            }else{
                session.rollback();
                cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_FAILED);
                cbsRtnInfo.setRtnMsg(resultCode + getTpsRtnErrorMsg(resultCode));
            }
            return cbsRtnInfo;
        } catch (SQLException e) {
            session.rollback();
            cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_FAILED);
            cbsRtnInfo.setRtnMsg("数据库处理异常");
            return cbsRtnInfo;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    //第三方服务处理：可根据交易号设置不同的超时时间
    private byte[] processThirdPartyServer(byte[] sendTpsBuf, String txnCode) throws Exception {
        String servIp = ProjectConfigManager.getInstance().getProperty("tps.server.ip");
        int servPort = Integer.parseInt(ProjectConfigManager.getInstance().getProperty("tps.server.port"));
        TpsSocketClient client = new TpsSocketClient(servIp, servPort);

        String timeoutCfg = ProjectConfigManager.getInstance().getProperty("tps.server.timeout.txn." + txnCode);
        if (timeoutCfg != null) {
            int timeout = Integer.parseInt(timeoutCfg);
            client.setTimeout(timeout);
        } else {
            timeoutCfg = ProjectConfigManager.getInstance().getProperty("tps.server.timeout");
            if (timeoutCfg != null) {
                int timeout = Integer.parseInt(timeoutCfg);
                client.setTimeout(timeout);
            }
        }

        logger.info("TPS Request:" + new String(sendTpsBuf, "GBK"));
        byte[] rcvTpsBuf = client.call(sendTpsBuf);
        logger.info("TPS Response:" + new String(rcvTpsBuf, "GBK"));
        return rcvTpsBuf;
    }
    private String getTpsRtnErrorMsg(String rtnCode) {
        BundleContext bundleContext = AppActivator.getBundleContext();
        URL url = bundleContext.getBundle().getEntry("rtncode.properties");

        Properties props = new Properties();
        try {
            props.load(url.openConnection().getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("错误码配置文件解析错误", e);
        }
        String property = props.getProperty(rtnCode);
        if (property == null) {
            property = "未定义对应的错误信息(错误码:" + rtnCode + ")";
        }
        return property;
    }

}
