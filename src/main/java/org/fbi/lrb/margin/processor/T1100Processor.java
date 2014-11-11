package org.fbi.lrb.margin.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.linking.codec.dataformat.SeperatedTextDataFormat;
import org.fbi.linking.processor.ProcessorException;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorRequest;
import org.fbi.linking.processor.standprotocol10.Stdp10ProcessorResponse;
import org.fbi.lrb.margin.domain.cbs.T1100Request.CbsTia1100;
import org.fbi.lrb.margin.domain.cbs.T1100Response.CbsToa1100;
import org.fbi.lrb.margin.enums.TxnRtnCode;
import org.fbi.lrb.margin.helper.MybatisFactory;
import org.fbi.lrb.margin.repository.dao.LrbMargActMapper;
import org.fbi.lrb.margin.repository.model.LrbMargAct;
import org.fbi.lrb.margin.repository.model.LrbMargActExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhanrui on 2014-11-11.
 * 1581100 ���˺Ų�ѯ����
 */
public class T1100Processor extends AbstractTxnProcessor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doRequest(Stdp10ProcessorRequest request, Stdp10ProcessorResponse response) throws ProcessorException, IOException {
        String txnDate = request.getHeader("txnTime").substring(0,8);
        String hostTxnsn = request.getHeader("serialNo");

        CbsTia1100 tia = new CbsTia1100();
        try {
            SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat(tia.getClass().getPackage().getName());
            tia = (CbsTia1100) dataFormat.fromMessage(new String(request.getRequestBody(), "GBK"), "CbsTia1100");
        } catch (Exception e) {
            logger.error("[sn=" + hostTxnsn + "] " + "��ɫҵ��ƽ̨�����Ľ�������.", e);
            throw new RuntimeException(e);
        }


        //ҵ���߼�����
        CbsRtnInfo cbsRtnInfo = null;
        try {
            CbsToa1100 toa = new CbsToa1100();
            cbsRtnInfo = processTxn(tia, toa);
            //��ɫƽ̨��Ӧ
            response.setHeader("rtnCode", cbsRtnInfo.getRtnCode().getCode());
            String cbsRespMsg = cbsRtnInfo.getRtnMsg();
            if (cbsRtnInfo.getRtnCode().equals(TxnRtnCode.TXN_EXECUTE_SECCESS)) {
                cbsRespMsg = marshalCbsResponseMsg(toa);
            }
            response.setResponseBody(cbsRespMsg.getBytes(response.getCharacterEncoding()));
        } catch (Exception e) {
            logger.error("[sn=" + hostTxnsn + "] " + "���״����쳣.", e);
            throw new RuntimeException("���״����쳣", e);
        }

    }


    private CbsRtnInfo processTxn(CbsTia1100 tia, CbsToa1100 toa) {
        CbsRtnInfo cbsRtnInfo = new CbsRtnInfo();
        SqlSessionFactory sqlSessionFactory = null;
        SqlSession session = null;
        try {
            sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
            session = sqlSessionFactory.openSession();

            LrbMargActMapper mapper = session.getMapper(LrbMargActMapper.class);

            LrbMargActExample example = new LrbMargActExample();
            example.createCriteria().andAcctnoEqualTo(tia.getAcctNo());
            List<LrbMargAct>  actList = mapper.selectByExample(example);
            if (actList.size() == 0) {
                cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_FAILED);
                cbsRtnInfo.setRtnMsg("�޴��˺�");
            } else if (actList.size() == 1) {
                LrbMargAct act = actList.get(0);
                toa.setInstCode(act.getInstcode());
                toa.setInstSeq(act.getInstseq());
                toa.setMatuDay(act.getMatuday());
                cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_SECCESS);
                cbsRtnInfo.setRtnMsg(TxnRtnCode.TXN_EXECUTE_SECCESS.getTitle());
            } else {
                cbsRtnInfo.setRtnCode(TxnRtnCode.TXN_EXECUTE_FAILED);
                cbsRtnInfo.setRtnMsg("�˺��ظ�");
            }

            return cbsRtnInfo;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
