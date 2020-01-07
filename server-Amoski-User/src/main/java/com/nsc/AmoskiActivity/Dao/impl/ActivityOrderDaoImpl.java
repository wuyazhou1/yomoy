package com.nsc.AmoskiActivity.Dao.impl;

import com.nsc.Amoski.entity.TbActivityOrderDetailsEntity;
import com.nsc.Amoski.util.StringUtils;
import com.nsc.AmoskiActivity.Dao.ActivityOrderDao;
import com.nsc.AmoskiActivity.Util.GenericRepositoryActivityImpl;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository("ActivityOrderDaoImpl")
public class ActivityOrderDaoImpl extends GenericRepositoryActivityImpl implements ActivityOrderDao {
    @Override
    public List activityOrderlist(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");

        if(!StringUtils.isEmpty(params.get("orderId"))){
            str.append(" and code = :orderId  ");
        }
        if(!StringUtils.isEmpty(params.get("basicsTile"))){
            str.append(" and BASICS_ID  in (select code from TB_ACTIVITY_BASICS where title like '%'|| :basicsTile ||'%')  ");
        }
        if(!StringUtils.isEmpty(params.get("memberId"))){
            str.append(" and member_id in (select id from amoski_user.t_member where name like '%'|| :memberId ||'%')  ");
        }
        if(!StringUtils.isEmpty(params.get("state"))){
            str.append(" and state = :state ");
        }
        List list = this.jdbcTemplate.queryForList(pageSql("select code,member_id,BASICS_ID,state,id,PRIMITIVE_MONEY_SUM from TB_ACTIVITY_ORDER where 1=1   "
                + str.toString()+" order by CREATE_DATA desc " ,params), params);
        return list;
    }

    public String selectColumn(){
        return " w.code a1,\n" +
                "(select name from amoski_user.t_member where id=w.member_id) a2," +
                "(select title from TB_ACTIVITY_BASICS where code=w.BASICS_ID) a3," +
                "(select listagg(INVOICE_NAME||'*'||count(1), '^') WITHIN GROUP(ORDER BY count(1)) from TB_ACTIVITY_ORDER_DETAILS where order_id=w.id group by INVOICE_NAME) a4," +
                "w.PRIMITIVE_MONEY_SUM a5," +
                "(select count(1) from TB_ACTIVITY_ORDER_DETAILS where order_id=w.id and state in (3,4,6))||'/'||(select count(1) from TB_ACTIVITY_ORDER_DETAILS where order_id=w.id) a6," +
                "(select nvl(sum(PRIMITIVE_MONEY),'0') from TB_ACTIVITY_ORDER_DETAILS where order_id=w.id and state=5) a7," +
                "(select nvl(sum(PRIMITIVE_MONEY),'0') from TB_ACTIVITY_ORDER_DETAILS where order_id=w.id and state in (3,4,6)) a8," +
                "(select SIGN_UP_ID from TB_ACTIVITY_ORDER_DETAILS where order_id=w.id and ROWNUM<=1) SIGN_UP_ID," +
                "w.state a9";
    }


    @Override
    public int activityOrderCount(Map<String, Object> params) {
        StringBuffer str = new StringBuffer("");

        if(!StringUtils.isEmpty(params.get("orderId"))){
            str.append(" and code = :orderId  ");
        }
        if(!StringUtils.isEmpty(params.get("basicsTile"))){
            str.append(" and BASICS_ID  in (select code from TB_ACTIVITY_BASICS where title like '%'|| :basicsTile ||'%')  ");
        }
        if(!StringUtils.isEmpty(params.get("memberId"))){
            str.append(" and member_id in (select id from amoski_user.t_member where name like '%'|| :memberId ||'%')  ");
        }
        if(!StringUtils.isEmpty(params.get("state"))){
            str.append(" and state = :state ");
        }
        return this.jdbcTemplate.queryForObject("select count(1) from TB_ACTIVITY_ORDER where 1=1  " + str.toString() , params,Integer.class);
    }


























    @Override
    public List activityOrderDatailList(Map<String, Object> params) {
        List list = this.jdbcTemplate.queryForList(pageSqlAll(
                "select c.VERIFICATION_CODE a1,c.name a2,c.IDENTITY_NUMBER a3,c.tel a4,c.CLUB a5,c.REGION a6," +
                        "c.REMAKE a7,b.INVOICE_NAME||'^￥'||b.PRIMITIVE_MONEY a8,b.state a9,b.id details_id," +
                        "a.id order_id,c.MODELS,c.DISPLACEMENT,c.CLOTHING_DIGITAL," +
                        "b.SIGN_UP_ID,a.code,b.PRIMITIVE_MONEY,d.PAYMENT_ACCOUNT,d.PAY_TYPE,0 service_money," +
                        "(select ID from TB_REFUND_AUDIT where ORDER_DETAILS_ID=b.id and state=1) REFUND_AUDIT_id,'' REMARK  " +
                        " from TB_ACTIVITY_ORDER a " +
                        " left join TB_ACTIVITY_ORDER_DETAILS b on a.id=b.order_id " +
                        " left join TB_ACTIVITY_SIGN_UP c on b.SIGN_UP_ID=c.id" +
                        " left join TB_ORDER_PAYMENT d on d.SERIAL_NUMBER=a.code" +
                        " where a.code = :id"  ,params), params);
        return list;
    }

    @Override
    public int activityOrderDatailCount(Map<String, Object> params) {
        return this.jdbcTemplate.queryForObject(
                "select count(1)" +
                        " from TB_ACTIVITY_ORDER a " +
                        " left join TB_ACTIVITY_ORDER_DETAILS b on a.id=b.order_id " +
                        " left join TB_ACTIVITY_SIGN_UP c on b.SIGN_UP_ID=c.id " +
                        " where a.code = :id"  , params,Integer.class);
    }

    @Override
    public Map<String, Object> queryOrderDatail(Map<String, Object> params) {
        //支付金额//账户金额//退款金额
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
                "select nvl(a.PRIMITIVE_MONEY_SUM,'0') payment," +
                        " nvl((select sum(b.PRIMITIVE_MONEY) from TB_ACTIVITY_ORDER_DETAILS b where b.STATE in ('3','4','6') and b.order_id=a.id),'0') account," +
                        " nvl((select sum(c.REFUND_MONEY) from TB_ACTIVITY_ORDER_DETAILS b left join (select ID,ORDER_DETAILS_ID,REFUND_MONEY,SERVICE_MONEY from TB_REFUND_AUDIT where state=3) c on b.id=c.ORDER_DETAILS_ID  where b.STATE=5 and b.order_id=a.id),'0') refund," +
                        " nvl((select sum(c.SERVICE_MONEY) from TB_ACTIVITY_ORDER_DETAILS b left join (select ID,ORDER_DETAILS_ID,REFUND_MONEY,SERVICE_MONEY from TB_REFUND_AUDIT where state=3) c on b.id=c.ORDER_DETAILS_ID  where b.STATE=5 and b.order_id=a.id),'0') SERVICE_MONEY," +
                        " a.state," +
                        " a.code " +
                        " from TB_ACTIVITY_ORDER a where code = :id", params);
        if(list.size()==0){
            return null;
        }
        return list.get(0);
    }

    @Override
    public Map<String, Object> queryActivityDatail(Map<String, Object> params) {
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(
                "select a.title,(select count(1) from TB_ACTIVITY_SIGN_UP b left join tb_activity_order_details c on c.SIGN_UP_ID=b.id where b.BASICS_ID=a.code and c.state in (3,4,6)) count," +
                        " b.COLLECTION_PLACE,b.collection_time,b.ACTIVITY_STOP," +
                        " '/'||c.PROJECT_URL||c.FILE_PATH_URL||'?filePath='||c.FILE_NAME_URL images," +
                        " b.BASICS_ID,a.id " +
                        " from TB_ACTIVITY_BASICS a " +
                        " left join TB_ACTIVITY_ORDINATION b on a.id=b.BASICS_ID" +
                        " left join TB_ACTIVITY_BILL_IMAGE c on a.id=c.BASICS_ID" +
                        " where a.code = (select BASICS_ID from TB_ACTIVITY_ORDER where code = :id)", params);
        if(list.size()==0){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Map<String, Object>> queryOrderHistoryDatail(Map<String, Object> params) {
        return this.jdbcTemplate.queryForList(
                "select * from (select CONTACT_NAME||'创建订单' describe,to_char(CREATE_DATA,'yyyy-mm-dd hh24:mi') CREATE_DATA from TB_ACTIVITY_ORDER where code = :id " +
                        " union all " +
                        " select (select name from amoski_user.t_member where id = a.member_id)||'支付￥'||nvl(a.AMOUNT,'0')||'  账户金额￥'||nvl(a.AMOUNT,'0') describe," +
                        " to_char(OPEN_DATE,'yyyy-mm-dd hh24:mi') CREATE_DATA from TB_ORDER_PAYMENT a where SERIAL_NUMBER= :id " +
                        " union all " +
                        " select a.CREATE_NAME||'申请退款' describe," +
                        " to_char(a.CREATE_DATA,'yyyy-mm-dd hh24:mi') CREATE_DATA" +
                        " from TB_Refund_Audit a " +
                        " left join TB_ACTIVITY_ORDER_DETAILS b on a.ORDER_DETAILS_ID=b.id" +
                        " left join TB_ACTIVITY_SIGN_UP c on b.SIGN_UP_ID=c.id" +
                        " where a.state=3 and b.code= :id " +
                        " union all " +
                        " select a.CREATE_NAME||'退款￥'||nvl(a.REFUND_MONEY,'0')||'   退款服务费￥'||nvl(a.SERVICE_MONEY,'0')||'    账户余额￥'||nvl(a.ACCOUNT_BALANCE,'0')||'    操作员：'||a.UPDATE_NAME describe," +
                        " to_char(a.UPDATE_DATE,'yyyy-mm-dd hh24:mi') CREATE_DATA" +
                        " from TB_Refund_Audit a left join TB_ACTIVITY_ORDER_DETAILS b on a.ORDER_DETAILS_ID=b.id  where a.state=3 and b.code= :id  "+
                        " union all " +
                        " select a.CREATE_NAME||'退款驳回  操作员：'||a.UPDATE_NAME describe," +
                        " to_char(a.UPDATE_DATE,'yyyy-mm-dd hh24:mi') CREATE_DATA" +
                        " from TB_Refund_Audit a left join TB_ACTIVITY_ORDER_DETAILS b on a.ORDER_DETAILS_ID=b.id  where a.state=2 and b.code= :id  " +
                        ") order by CREATE_DATA"
                , params);
    }

    @Override
    public TbActivityOrderDetailsEntity queryOrderDatailByid(Map<String, Object> parentMap) {
        TbActivityOrderDetailsEntity o = (TbActivityOrderDetailsEntity)this.jdbcTemplate.queryForObject(
                "select * from TB_ACTIVITY_ORDER_DETAILS where id = :DETAILS_ID ", parentMap, new BeanPropertyRowMapper(TbActivityOrderDetailsEntity.class));
        return o;
    }

    @Override
    public void updateRefundMoneyById(Map<String, Object> parentMap) {
        Integer count = this.jdbcTemplate.update("update TB_REFUND_AUDIT set STATE=3,\n" +
                " UPDATE_DATE=sysdate," +
                " UPDATE_NAME= :USER_NAME," +
                " REFUND_MONEY= :REFUND_MONEY ," +
                " ACCOUNT_BALANCE = (select sum(PRIMITIVE_MONEY) from TB_ACTIVITY_ORDER_DETAILS where state in (3,4,6) and order_id=(select order_id from TB_ACTIVITY_ORDER_DETAILS where id= :DETAILS_ID ))," +
                " SERVICE_MONEY= :SERVICE_MONEY " +
                " where id = :REFUND_AUDIT_ID" +
                " and STATE=1",parentMap);
        if(count!=1){
            throw new RuntimeException("退款数据错误，请刷新数据后重试");
        }
    }

    @Override
    public void updateOrderStateById(Map<String, Object> parentMap) {
        Integer count = this.jdbcTemplate.update("update TB_ACTIVITY_ORDER_DETAILS set STATE=5 where id = :DETAILS_ID  and state=4",parentMap);
        if(count!=1){
            throw new RuntimeException("订单数据错误，请刷新数据后重试");
        }
    }

    @Override
    public void updateRefundMoneyFalseById(Map<String, Object> parentMap) {
        this.jdbcTemplate.update("update TB_REFUND_AUDIT set STATE=2," +
                " UPDATE_DATE=sysdate," +
                " UPDATE_NAME= :USER_NAME" +
                " where  id = :REFUND_AUDIT_ID  and STATE=1" +
                " and STATE=1",parentMap);
    }

    @Override
    public void updateOrderDatailsFalseById(Map<String, Object> parentMap) {
        this.jdbcTemplate.update("update TB_ACTIVITY_ORDER set state=3 where code in (select code from TB_ACTIVITY_ORDER_DETAILS where id = :DETAILS_ID)  and state=4",parentMap);
        this.jdbcTemplate.update("update TB_ACTIVITY_ORDER_DETAILS set state=3  where id = :DETAILS_ID  and state=4",parentMap);
    }

    @Override
    public void updateOrderByDatailsId(Map<String, Object> parentMap) {
        this.jdbcTemplate.update("MERGE INTO TB_ACTIVITY_ORDER a1 " +
                " USING (select count(1) count1, " +
                " (select count(1) from TB_ACTIVITY_ORDER_DETAILS where code=a.code) count2 " +
                " ,a.code from TB_ACTIVITY_ORDER_DETAILS a where code in (select code from TB_ACTIVITY_ORDER_DETAILS where id = :DETAILS_ID ) and state='5' group by code) a2" +
                " ON (a2.count1=a2.count2 and a1.code=a2.code) " +
                " WHEN MATCHED THEN " +
                " UPDATE " +
                " SET state = 5",parentMap);

    }

}
