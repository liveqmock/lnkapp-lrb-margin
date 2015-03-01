package org.fbi.lrb.margin.server.processor;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.lrb.margin.helper.MybatisFactory;
import org.fbi.lrb.margin.repository.dao.LrbMargActMapper;
import org.fbi.lrb.margin.repository.model.LrbMargAct;
import org.fbi.lrb.margin.repository.model.LrbMargActKey;
import org.fbi.lrb.margin.domain.tps.TIAG00003;
import org.fbi.lrb.margin.domain.tps.TOAG00003;
import org.fbi.lrb.margin.server.tcpserver.Processor;
import org.fbi.lrb.margin.server.tcpserver.TxnContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by zhanrui on 2014/11/6.
 * �������˺�
 */
public class G00003Processor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(G00003Processor.class);

    @Override
    public void service(TxnContext ctx) {
        TOAG00003 toa = new TOAG00003();
        toa.getHead().setTransCode("G00003");
        toa.getHead().setTransDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        toa.getHead().setTransTime(new SimpleDateFormat("HHmmdd").format(new Date()));
        toa.getHead().setSeqNo(new SimpleDateFormat("yyyyMMddHHmmddSSS").format(new Date()));  //Ĭ��ֵ

        try {
            String msgtia = ctx.getMsgtia();
            TIAG00003 tia = (TIAG00003) new TIAG00003().toBean(msgtia);

            processTxn(tia, toa);

            //����toa��ˮ����tiaһ��
            toa.getHead().setSeqNo(tia.getHead().getSeqNo());

            ctx.setMsgtoa(toa.toXml(toa));
        } catch (Exception e) {
            logger.error("���״���ʧ��", e);
            toa.getBody().setResult("99");
            toa.getBody().setAddWord("ϵͳ���� ���״���ʧ�ܡ�");
            ctx.setMsgtoa(toa.toXml(toa));
        }
    }

    private void processTxn(TIAG00003 tia, TOAG00003 toa) {
        SqlSessionFactory sqlSessionFactory = null;
        SqlSession session = null;
        try {
            sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
            session = sqlSessionFactory.openSession();
            session.getConnection().setAutoCommit(false);
            LrbMargActMapper mapper = session.getMapper(LrbMargActMapper.class);

            LrbMargActKey key = new LrbMargActKey();
            key.setInstcode(tia.getBody().getInstCode());
            key.setInstseq(tia.getBody().getInstSeq());
            LrbMargAct margAct = mapper.selectByPrimaryKey(key);
            if (margAct != null) {//�ظ�
                toa.getBody().setResult("01");
                toa.getBody().setAddWord("�����ʺ�+����� �ظ�");
                return;
            }

            margAct = new LrbMargAct();
            margAct.setInstcode(tia.getBody().getInstCode());
            margAct.setInstseq(tia.getBody().getInstSeq());
            margAct.setMatuday(tia.getBody().getMatuDay());

//            String acctno = "37198" + tia.getBody().getInstSeq();
            String acctno = new StringBuilder().append(tia.getBody().getInstCode()).append(new SimpleDateFormat("yyyyMMdd").format(new Date())).append(RandomStringUtils.randomNumeric(4)).toString();
            margAct.setAcctno(acctno);
            margAct.setActSts("0");
            margAct.setActBal(new BigDecimal("0"));
            margAct.setIntcPdt(new BigDecimal("0"));
            margAct.setOpenActDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));

            margAct.setIfTiaSn(tia.getHead().getSeqNo()); //����ˮ����TIA��ͬ
            margAct.setRecversion(0);
            mapper.insert(margAct);

            toa.getBody().setAcctNo(acctno);
            toa.getBody().setResult("00");
            toa.getBody().setAddWord("����ɹ�");
            session.commit();
        } catch (Exception e) {
            if (session != null) {
                session.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
