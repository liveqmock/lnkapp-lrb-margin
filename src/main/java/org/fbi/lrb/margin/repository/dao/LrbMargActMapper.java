package org.fbi.lrb.margin.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.fbi.lrb.margin.repository.model.LrbMargAct;
import org.fbi.lrb.margin.repository.model.LrbMargActExample;
import org.fbi.lrb.margin.repository.model.LrbMargActKey;

public interface LrbMargActMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int countByExample(LrbMargActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int deleteByExample(LrbMargActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int deleteByPrimaryKey(LrbMargActKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int insert(LrbMargAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int insertSelective(LrbMargAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    List<LrbMargAct> selectByExample(LrbMargActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    LrbMargAct selectByPrimaryKey(LrbMargActKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int updateByExampleSelective(@Param("record") LrbMargAct record, @Param("example") LrbMargActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int updateByExample(@Param("record") LrbMargAct record, @Param("example") LrbMargActExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int updateByPrimaryKeySelective(LrbMargAct record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table LNK.LRB_MARG_ACT
     *
     * @mbggenerated Tue Nov 11 18:16:54 CST 2014
     */
    int updateByPrimaryKey(LrbMargAct record);
}