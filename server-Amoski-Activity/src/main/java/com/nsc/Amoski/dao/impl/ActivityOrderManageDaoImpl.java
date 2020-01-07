package com.nsc.Amoski.dao.impl;

import com.nsc.Amoski.dao.ActivityOrderManageDao;
import com.nsc.Amoski.dto.ActivityBasicDto;
import com.nsc.Amoski.dto.ActivityOrderDto;
import com.nsc.Amoski.dto.ActivityOrderDto1;
import com.nsc.Amoski.dto.ElectronicTicketDto;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbActivitySignUpEntity;
import com.nsc.Amoski.entity.TbActivityTimeHistoryEntity;
import com.nsc.Amoski.entity.TbRefundAuditEntity;
import com.nsc.Amoski.repository.GenericRepositoryImpl;
import com.nsc.Amoski.util.RegUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ActivityOrderManageDaoImpl extends GenericRepositoryImpl implements ActivityOrderManageDao {

    /**
     * 查询所有活动列表
     * @param param 查询条件 查询所有活动列表
     * @return  查询所有活动列表
     */
    public PagingBean queryActivityList(ActivityBasicDto param){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder sql=new StringBuilder("select t.* from (select b.id,b.code,b.org_code,b.current_traffic,b.daily_visits,b.total_visits,b.type,b.title,b.nature,b.state,b.describe,b.notice,b.create_name,b.create_data,b.update_name,b.update_date " +
                ",s.type playType,s.destination,s.path_point,s.activity_intensity,s.equipment_requirements,s.details_activities " +
                ",o.mandatory_field,o.scope_registration,o.collection_place,o.collection_time,o.activity_start,o.activity_stop,o.activity_end,o.number_limitation,o.show_number " +
                ",case when o.activity_start>sysdate then 1 else 2 end orderDes from TB_ACTIVITY_BASICS b left join tb_activity_synopsis s on b.id=s.basics_id left join tb_activity_ordination o on b.id=o.basics_id " +
                "where b.state=2) t order by t.orderDes");
        if("2".equals(param.getType())){
            sql.append(",t.create_data desc");
        }
        sql.append(",t.Total_Visits desc");

        StringBuilder countSql=new StringBuilder("select count(*) from TB_ACTIVITY_BASICS b where b.state=2 ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("pageSize",param.getPageSize());
        map.put("start",param.getStart());
        //查询条件
        String querySql=sql.toString(),queryCountSql=countSql.toString();
        logger.info(">>>>queryActivityList>>>>>queryCount sql:"+queryCountSql+">>>>>>>paramMap:"+map.toString());
        logger.info(">>>>queryActivityList>>>>>query sql:"+querySql+">>>>>>>");
        int count= this.jdbcTemplate.queryForObject(queryCountSql,map,Integer.class);
        List<ActivityBasicDto> list=this.jdbcTemplate.query(pageSql(querySql,map), map, new BeanPropertyRowMapper<ActivityBasicDto>(ActivityBasicDto.class));
        PagingBean bean=new PagingBean();
        bean.setData(list);
        bean.setRecordsTotal(count);
        bean.setRecordsFiltered(count);
        logger.info(">>>>queryActivityList end>>>>>>>>>>>>result:"+bean.toString());
        return bean;
    }

    /**
     * 查询用户是否已报名活动 (是否存在已支付订单)
     * @return  查询用户是否已报名活动
     */
    @Override
    public ActivityOrderDto queryUserExistOrder(ActivityOrderDto info) {
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder querySql=new StringBuilder("select * from  tb_activity_sign_up s left join tb_activity_order o on s.order_code=o.code where o.state=3 ");
        Map<String,Object> map=new HashMap<String, Object>();
        if(!regUtil.isNull(info.getBasicsId())){
            querySql.append(" and s.basics_id=:basicsId");
            map.put("basicsId",info.getBasicsId());
        }
        if(!regUtil.isNull(info.getIdentityNumber())){
            querySql.append(" and s.identity_number=:idNumber");
            map.put("idNumber",info.getIdentityNumber());
        }
        if(!regUtil.isNull(info.getTel())){
            querySql.append(" and s.tel=:tel");
            map.put("tel",info.getTel());
        }
        logger.info(">>>>queryUserExistOrder>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<ActivityOrderDto> list=this.jdbcTemplate.query(querySql.toString(), map, new BeanPropertyRowMapper<ActivityOrderDto>(ActivityOrderDto.class));
        logger.info(">>>>queryUserExistOrder>>>>>>>>>>>>result:"+list);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    /**
     * 修改订单状态
     * @return  修改订单状态
     */
    @Override
    public void updateOrderState(ActivityOrderDto info) {
        StringBuilder querySql=new StringBuilder("update TB_ACTIVITY_ORDER_DETAILS set state=:state where order_id=:orderId");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("state",info.getState());
        map.put("orderId",info.getId());
        logger.info(">>>>updateOrderState>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        this.jdbcTemplate.update(querySql.toString(),map);
    }

    /**
     * 查询用户所有活动报名订单信息
     * @return  查询用户所有活动报名订单信息
     */
    public List<ActivityOrderDto1> queryOrder(ActivityOrderDto order){
        RegUtil regUtil=RegUtil.getSingleton();// inner join tb_activity_ordination or on b.id=or.basics_id
        StringBuilder querySql=new StringBuilder("select o.*,bi.file_name_url,od.collection_place,to_char(od.collection_time,'yyyy-mm-dd hh24:mi:ss') collection_time,to_char(od.activity_start,'yyyy-mm-dd hh24:mi:ss') activity_start,to_char(od.activity_stop,'yyyy-mm-dd hh24:mi:ss') activity_stop,to_char(od.activity_end,'yyyy-mm-dd hh24:mi:ss') activity_end,b.type, b.title, b.nature, b.describe, b.notice from tb_activity_order o " +
                " left join TB_ACTIVITY_BASICS b on o.basics_id=b.code " +
                " left join tb_activity_ordination od on b.id=od.basics_id " +
                " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE where type='1' group by basics_id) bi on bi.basics_id=b.id  "+
                " where o.member_id=:memberId");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("memberId",order.getMemberId());
        if(!regUtil.isNull(order.getId())){//订单id
            querySql.append(" and o.id=:id");
            map.put("id",order.getId());
        }
        querySql.append(" order by o.state,o.CREATE_DATA desc");
        logger.info(">>>>queryOrder>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<ActivityOrderDto1> list=this.jdbcTemplate.query(querySql.toString(), map, new BeanPropertyRowMapper<ActivityOrderDto1>(ActivityOrderDto1.class));
        logger.info(">>>>queryOrder>>>>>>>>>>>>result:"+list);
        return list;
    }

    /**
     * 更新失效订单
     * @return  更新失效订单
     */
    public void updateTimeoutOrder(ActivityOrderDto order,int type){
        StringBuilder querySql=new StringBuilder("update TB_ACTIVITY_ORDER_DETAILS de set state=6 where state=3 and " +
                "exists(select o.id from TB_ACTIVITY_ORDER o left join TB_ACTIVITY_BASICS b on o.basics_id=b.code left join tb_activity_ordination od on b.id=od.basics_id " +
                " where de.order_id=o.id and o.member_id=:memberId and o.state=3 and to_number(sysdate-od.ACTIVITY_STOP)>0 )");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("memberId",order.getMemberId());
        if(type==1){
            querySql=new StringBuilder("update TB_ACTIVITY_ORDER o set state=6 where member_id=:memberId and state=3 and " +
                    "exists(select b.id from TB_ACTIVITY_BASICS b left join tb_activity_ordination od on b.id=od.basics_id " +
                    "where o.basics_id=b.code and to_number(sysdate-od.ACTIVITY_STOP)>0 )");
        }
        logger.info(">>>>updateOrderState>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        this.jdbcTemplate.update(querySql.toString(),map);
    }

    /**
     * 查询用户退票详情信息
     * @return  查询用户退票详情信息
     */
    public List<TbRefundAuditEntity> queryOrderRefundDetail(ActivityOrderDto order){
        RegUtil regUtil=RegUtil.getSingleton();// inner join tb_activity_ordination or on b.id=or.basics_id
        StringBuilder querySql=new StringBuilder("");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("memberId",order.getMemberId());
        if(!regUtil.isNull(order.getId())){//订单id
            querySql.append(" and o.order_id=:orderId");
            map.put("orderId",order.getId());
        }
        logger.info(">>>>queryOrderRefundDetail>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<TbRefundAuditEntity> list=this.jdbcTemplate.query(querySql.toString(), map, new BeanPropertyRowMapper<TbRefundAuditEntity>(TbRefundAuditEntity.class));
        logger.info(">>>>queryOrderRefundDetail>>>>>>>>>>>>result:"+list);
        return list;
    }


    /**
     * 查询用户订单详情信息
     * @return  查询用户订单详情信息
     */
    public List<ActivityOrderDto> queryOrderDetail(ActivityOrderDto order,int type){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder querySql=new StringBuilder("select s.*,o.primitive_money,o.state,o.id,refundTableFied from TB_ACTIVITY_ORDER_DETAILS o " +
                "inner join TB_ACTIVITY_SIGN_UP s on s.id=o.SIGN_UP_ID " +
                "");
        String fiedStr="";
        StringBuilder whereSql=new StringBuilder(" where o.order_id=:orderId ");
        if(type==1){
            whereSql.append(" and (o.state=4 or o.state=5 )");
            querySql.append(" left join TB_REFUND_AUDIT ra on ra.order_details_id=o.id ");
            fiedStr=",ra.refund_money refundMoney , ra.state restate, ra.update_name reupdateName, ra.update_date reupdateDate, ra.create_name recreateName, ra.create_data recreateData";
        }
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("orderId",order.getId());
        String sql = querySql.append(whereSql).toString().replace(",refundTableFied",fiedStr);
        logger.info("\n\n查询用户订单详情信息querySql.append(whereSql).toString().replace(\",refundTableFied\",fiedStr)==>"+sql
        +"\n查询参数paretnMap==>"+map);
        List<ActivityOrderDto> list=this.jdbcTemplate.query(
                //querySql.append(whereSql).toString().replace(",refundTableFied",fiedStr)
                sql
                , map,
                new BeanPropertyRowMapper<ActivityOrderDto>(ActivityOrderDto.class));
        logger.info(">>>>queryOrderDetail>>>>>>>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询电子票列表
     * @return  查询电子票列表
     */
    public List<ActivityOrderDto1> queryElectronicTicket(ActivityOrderDto order){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder querySql=new StringBuilder("select o.*,bi.file_name_url,od.collection_place,to_char(od.collection_time,'yyyy-MM-dd hh24:mi:ss') collection_time,to_char(od.activity_start,'yyyy-MM-dd hh24:mi:ss') activity_start ,to_char(od.activity_stop,'yyyy-MM-dd hh24:mi:ss') activity_stop,to_char(od.activity_end,'yyyy-MM-dd hh24:mi:ss') activity_end,b.type, " +
                "b.title, b.nature, b.describe, b.notice,de.id,de.INVOICE_ID,de.INVOICE_NAME,de.PRIMITIVE_MONEY,case when to_number(sysdate-od.ACTIVITY_STOP)>0 or de.state<>3 then 0 else 1 end effective " +
                " from tb_activity_order o left join TB_ACTIVITY_BASICS b on o.basics_id=b.code left join tb_activity_ordination od on b.id=od.basics_id " +
                " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE group by basics_id) bi on bi.basics_id=b.id  "+
                " left join TB_ACTIVITY_ORDER_DETAILS de on de.order_id=o.id ");
        StringBuilder whereSql=new StringBuilder(" where o.member_id=:memberId and de.state>2 order by o.CREATE_DATA desc ");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("memberId",order.getMemberId());
        logger.info(">>>>queryElectronicTicket>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<ActivityOrderDto1> list=this.jdbcTemplate.query(querySql.append(whereSql).toString(), map, new BeanPropertyRowMapper<ActivityOrderDto1>(ActivityOrderDto1.class));
        logger.info(">>>>queryElectronicTicket>>>>>>>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询电子详情
     * @return  查询电子详情
     */
    public List<ElectronicTicketDto> queryElectronicTicketDetail(ElectronicTicketDto order){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder querySql=new StringBuilder("select o.*,s.name,s.VERIFICATION_CODE,oc.CREATE_TIME vacreateTime,oc.STATE vastate,oc.SCHEDULE_TIME_ID, " +
                "bi.file_name_url,b.title,t.START_TIME,t.STOP_TIME,t.INTRODUCE_TYPE,t.INTRODUCE,s.basics_id,s.tel SIGN_UP_tel " +
                //",(select )" +
                "from TB_ACTIVITY_ORDER_DETAILS o left join TB_ACTIVITY_SIGN_UP s on s.id=o.SIGN_UP_ID left join TB_VALID_ORDER_CODE oc on o.id=oc.DETAIL_ID " +
                "left join TB_ACTIVITY_TIME_HISTORY t on t.id=oc.SCHEDULE_TIME_ID left join TB_ACTIVITY_BASICS b on s.basics_id=b.code " +
                " left join (select basics_id,max(FILE_NAME_URL) file_name_url from TB_ACTIVITY_BILL_IMAGE group by basics_id) bi on bi.basics_id=b.id ");
        StringBuilder whereSql=new StringBuilder(" where 1=1 ");
        Map<String,Object> map=new HashMap<String, Object>();
        if(!regUtil.isNull(order.getId())){
            map.put("id",order.getId());
            whereSql.append("and o.id=:id");
        }
        if(!regUtil.isNull(order.getOrderId())){
            map.put("orderId",order.getOrderId());
            whereSql.append("and o.order_id=:orderId");
        }

        logger.info(">>>>queryElectronicTicketDetail>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<ElectronicTicketDto> list=this.jdbcTemplate.query(querySql.append(whereSql).toString(), map, new BeanPropertyRowMapper<ElectronicTicketDto>(ElectronicTicketDto.class));
        logger.info(">>>>queryElectronicTicketDetail>>>>>>>>>>>>result:"+list);
        return list;
    }

    /**
     * 查询活动报名订单提醒
     * @return  查询活动报名订单提醒
     */
    public List<ActivityOrderDto> queryActivityOrderWarning(){
        RegUtil regUtil=RegUtil.getSingleton();
        StringBuilder querySql=new StringBuilder("select * from tb_activity_basics b inner join tb_activity_ordination od on b.id=od.basics_id " +
                "inner join tb_activity_order o on b.code=o.basics_id inner join tb_activity_order_details de on o.id=de.order_id " +
                "inner join tb_activity_sign_up s on de.sign_up_id=s.id " +
                "inner join amoski_user.t_member m on s.tel=m.tel or s.tel=m.loginname where o.isremind=1 and to_char(sysdate+1,'yyyy-mm-dd')=to_char(od.activity_start,'yyyy-mm-dd')");
        Map<String,Object> map=new HashMap<String, Object>();
        logger.info(">>>>queryActivityOrderWarning>>>>>query sql:"+querySql+">>>>>>>paramMap:"+map.toString());
        List<ActivityOrderDto> list=this.jdbcTemplate.query(querySql.toString(), map, new BeanPropertyRowMapper<ActivityOrderDto>(ActivityOrderDto.class));
        logger.info(">>>>queryActivityOrderWarning>>>>>>>>>>>>result:"+list);
        return list;
    }

}
