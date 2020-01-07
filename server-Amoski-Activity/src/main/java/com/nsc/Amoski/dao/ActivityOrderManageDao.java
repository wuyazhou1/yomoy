package com.nsc.Amoski.dao;


import com.nsc.Amoski.dto.*;
import com.nsc.Amoski.entity.PagingBean;
import com.nsc.Amoski.entity.TbRefundAuditEntity;

import java.util.List;

public interface ActivityOrderManageDao {

    /**
     * 查询所有活动列表
     * @param param 查询条件 查询所有活动列表
     * @return  查询所有活动列表
     */
    PagingBean queryActivityList(ActivityBasicDto param);

    /**
     * 查询用户是否已报名活动 (是否存在已支付订单)
     * @return  查询用户是否已报名活动
     */
    ActivityOrderDto queryUserExistOrder(ActivityOrderDto info);

    /**
     * 修改订单状态
     * @return  修改订单状态
     */
    void updateOrderState(ActivityOrderDto info);

    /**
     * 查询用户所有活动报名订单信息
     * @return  查询用户所有活动报名订单信息
     */
    List<ActivityOrderDto1> queryOrder(ActivityOrderDto order);

    /**
     * 更新失效订单
     * @return  更新失效订单
     */
    void updateTimeoutOrder(ActivityOrderDto order,int type);

    /**
     * 查询用户退票详情信息
     * @return  查询用户退票详情信息
     */
    List<TbRefundAuditEntity> queryOrderRefundDetail(ActivityOrderDto order);

    /**
     * 查询用户订单详情信息
     * @return  查询用户订单详情信息
     */
    List<ActivityOrderDto> queryOrderDetail(ActivityOrderDto order,int type);

    /**
     * 查询电子票列表
     * @return  查询电子票列表
     */
    List<ActivityOrderDto1> queryElectronicTicket(ActivityOrderDto order);

    /**
     * 查询电子详情
     * @return  查询电子详情
     */
    List<ElectronicTicketDto> queryElectronicTicketDetail(ElectronicTicketDto order);

    /**
     * 查询活动报名订单提醒
     * @return  查询活动报名订单提醒
     */
    List<ActivityOrderDto> queryActivityOrderWarning();
}
