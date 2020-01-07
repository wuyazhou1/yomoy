package com.nsc.Amoski.service.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.message.MessageListResult;
import cn.jmessage.api.message.SendMessageResult;
import com.nsc.Amoski.entity.jg.JGIMMessage;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
public interface JGIMMessagesService {

    ResponseWrapper retractMessage(String username, long msgId) throws APIConnectionException, APIRequestException;

    MessageListResult getMessageList(int count, String begin_time, String end_time) throws APIConnectionException, APIRequestException;

    MessageListResult getMessageListByCursor(String cursor) throws APIConnectionException, APIRequestException;

    MessageListResult getUserMessages(String username, int count, String begin_time, String end_time) throws APIConnectionException, APIRequestException;

    MessageListResult getUserMessagesByCursor(String username, String cursor) throws APIConnectionException, APIRequestException;

    SendMessageResult sendTextMessage(String targetType, String targetId, String formType, String formId, String text) throws APIConnectionException, APIRequestException;

    SendMessageResult sendImageMessage(String targetType, String targetId, String formType, String formId, JGIMMessage.Body body) throws APIConnectionException, APIRequestException;

    public SendMessageResult sendVoiceMessage(String targetType, String targetId, String formType, String formId, JGIMMessage.Body body) throws APIConnectionException, APIRequestException;

}
