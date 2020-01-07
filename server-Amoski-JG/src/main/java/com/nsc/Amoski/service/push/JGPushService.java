package com.nsc.Amoski.service.push;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.device.TagListResult;
import cn.jpush.api.push.CIDResult;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
public interface JGPushService {

    CIDResult getCidList(int count, String type) throws APIConnectionException, APIRequestException;

    TagListResult getTagList() throws APIConnectionException, APIRequestException;

    PushResult sendPush(Platform platform, Audience audience, Notification notification, Message message, SMS sms, String cid) throws APIConnectionException, APIRequestException;

    PushResult sendPushToAll(String text) throws APIConnectionException, APIRequestException;

}
