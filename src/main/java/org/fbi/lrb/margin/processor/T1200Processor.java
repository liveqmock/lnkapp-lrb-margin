package org.fbi.lrb.margin.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.fbi.lrb.margin.domain.cbs.T1200Request.CbsTia1200;
import org.fbi.lrb.margin.enums.TxnRtnCode;
import org.fbi.lrb.margin.helper.MybatisFactory;
import org.fbi.lrb.margin.repository.dao.LrbMargTxnMapper;
import org.fbi.lrb.margin.repository.model.LrbMargTxn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

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


    private CbsRtnInfo processTxn(CbsTia1200 tia, String txnDate,  String hostTxnsn) {
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
            txn.setIndate(tia.getInDate());
            txn.setInmemo(tia.getInMemo());  //子帐号

            mapper.insert(txn);

            //TODO 国土局端处理


            //交易处理成功
            session.commit();
            cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_SECCESS);
            cbsRtnInfo.setRtnMsg(TxnRtnCode.TXN_EXECUTE_SECCESS.getTitle());
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

}
