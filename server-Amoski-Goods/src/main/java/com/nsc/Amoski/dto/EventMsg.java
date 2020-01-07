package com.nsc.Amoski.dto;

import lombok.Data;
import sun.security.krb5.internal.Ticket;

@Data
public class EventMsg extends BaseMsg{


    // 事件列表：CLICK(点击自定义菜单)/subscribe(关注)/unsubscribe(取消关注)/SCAN(扫描二维码)/LOCATION(上报地理位置)
    private String event;//事件类型

    private String eventKey;//事件KEY值，qrscene_为前缀，后面为二维码的参数值
    private String ticket;//二维码的ticket，可用来换取二维码图片

    //上报地理位置
    private String latitude;//地理位置纬度
    private String longitude;//地理位置经度
    private String precision;//地理位置精度
}
