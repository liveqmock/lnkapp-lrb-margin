package org.fbi.lrb.margin.server.processor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.fbi.lrb.margin.helper.MybatisFactory;
import org.fbi.lrb.margin.repository.dao.LrbMargTxnMapper;
import org.fbi.lrb.margin.repository.model.LrbMargTxn;
import org.fbi.lrb.margin.repository.model.LrbMargTxnExample;
import org.fbi.lrb.margin.domain.tps.TIAG00005;
import org.fbi.lrb.margin.domain.tps.TOAG00005;
import org.fbi.lrb.margin.server.tcpserver.Processor;
import org.fbi.lrb.margin.server.tcpserver.TxnContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhanrui on 2014/11/6.
 * 查询子账号到账明细
 */
public class G00005Processor implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(G00005Processor.class);

    @Override
    public void service(TxnContext ctx) {
        TOAG00005 toa = new TOAG00005();
        toa.getHead().setTransCode("G00005");
        toa.getHead().setTransDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        toa.getHead().setTransTime(new SimpleDateFormat("HHmmdd").format(new Date()));
        toa.getHead().setSeqNo(new SimpleDateFormat("yyyyMMddHHmmddSSS").format(new Date()));  //默认值

        try {
            String msgtia = ctx.getMsgtia();
            TIAG00005 tia = (TIAG00005) new TIAG00005().toBean(msgtia);

            processTxn(tia, toa);

            //重置toa流水号与tia一致
            toa.getHead().setSeqNo(tia.getHead().getSeqNo());

            ctx.setMsgtoa(toa.toXml(toa));
        } catch (Exception e) {
            logger.error("交易处理失败", e);
            toa.getBody().setResult("99");
            ctx.setMsgtoa(toa.toXml(toa));
        }
    }

    private void processTxn(TIAG00005 tia, TOAG00005 toa) {
        SqlSessionFactory sqlSessionFactory = null;
        SqlSession session = null;
        try {
            sqlSessionFactory = MybatisFactory.ORACLE.getInstance();
            session = sqlSessionFactory.openSession();
            LrbMargTxnMapper mapper = session.getMapper(LrbMargTxnMapper.class);

            LrbMargTxnExample example = new LrbMargTxnExample();
            example.createCriteria()
                    .andInstcodeEqualTo(tia.getBody().getInstCode())
                    .andInmemoEqualTo(tia.getBody().getInMemo());

            List<LrbMargTxn> txnList = mapper.selectByExample(example);

            List<TOAG00005.MsgBody.Entity> listEntity = new ArrayList<>();
            int count = 0;
            for (LrbMargTxn txn : txnList) {
                TOAG00005.MsgBody.Entity entity = new TOAG00005.MsgBody.Entity();
                entity.setInDate(txn.getIndate());
                entity.setInAmount(txn.getInamount().toString());
                entity.setInName(txn.getInname());
                entity.setInAcct(txn.getInacct());
                entity.setInBankFLCode(txn.getInbankflcode());
                entity.rowNum = "" + count;
                listEntity.add(entity);
                count++;
            }
            toa.getBody().setResult("00");
            toa.getBody().setInMemo(tia.getBody().getInMemo());
            toa.getBody().setCount("" + count);
            toa.getBody().setListEntity(listEntity);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
