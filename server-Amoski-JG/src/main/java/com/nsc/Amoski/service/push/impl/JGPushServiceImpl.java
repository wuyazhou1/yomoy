package com.nsc.Amoski.service.push.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.TagListResult;
import cn.jpush.api.push.CIDResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.nsc.Amoski.service.push.JGPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Service
public class JGPushServiceImpl implements JGPushService {

    @Autowired
    private JPushClient jPushClient;

    /**
     * 根据类型获取唯一标识符列表
     *
     * @param count
     * @param type
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public CIDResult getCidList(int count, String type) throws APIConnectionException, APIRequestException {
        CIDResult cidResult = jPushClient.getCidList(count, type);
        return cidResult;
    }

    /**
     * 获取标签列表
     *
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public TagListResult getTagList() throws APIConnectionException, APIRequestException {
        TagListResult tagListResult = jPushClient.getTagList();
        return tagListResult;
    }

    /**
     * 推送消息
     *
     * @param platform
     * @param audience
     * @param notification
     * @param message
     * @param sms
     * @param cid
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public PushResult sendPush(Platform platform, Audience audience, Notification notification, Message message, SMS sms, String cid) throws APIConnectionException, APIRequestException {
        PushPayload.Builder builder = PushPayload.newBuilder();
        //推送平台设置
        builder.setPlatform(platform);
        //推送设备指定
        builder.setAudience(audience);
        //通知内容体
        if (null != notification) builder.setNotification(notification);
        //消息内容体
        if (null != message) builder.setMessage(message);
        //短信渠道补充送达内容体
        if (null != sms) builder.setSMS(sms);
        //用于防止 api 调用端重试造成服务端的重复推送而定义的一个标识符。
        if (null != cid) builder.setCid(cid);
        PushResult pushResult = jPushClient.sendPush(builder.build());
        return pushResult;
    }

    /**
     * 推送通知到所有平台
     *
     * @param text
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    public PushResult sendPushToAll(String text) throws APIConnectionException, APIRequestException {
        PushPayload.Builder builder = PushPayload.newBuilder();
        builder.setPlatform(Platform.all());
        builder.setAudience(Audience.all());
        builder.setNotification(Notification.alert(text));
        PushResult pushResult = jPushClient.sendPush(builder.build());
        return pushResult;
    }

}
