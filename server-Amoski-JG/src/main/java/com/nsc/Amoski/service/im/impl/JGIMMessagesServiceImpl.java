package com.nsc.Amoski.service.im.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.common.model.message.MessageBody;
import cn.jmessage.api.message.MessageListResult;
import cn.jmessage.api.message.MessageType;
import cn.jmessage.api.message.SendMessageResult;
import com.nsc.Amoski.entity.jg.JGIMMessage;
import com.nsc.Amoski.service.im.JGIMMessagesService;
import com.nsc.Amoski.utlis.JGUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Service
public class JGIMMessagesServiceImpl implements JGIMMessagesService {

    @Autowired
    private JMessageClient jMessageClient;

    /**
     * 撤回消息
     *
     * @param username
     * @param msgId
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public ResponseWrapper retractMessage(String username, long msgId) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.retractMessage(username, msgId);
        return responseWrapper;
    }

    /**
     * 获取消息列表
     *
     * @param count
     * @param begin_time
     * @param end_time
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public MessageListResult getMessageList(int count, String begin_time, String end_time) throws APIConnectionException, APIRequestException {
        MessageListResult messageListResult = jMessageClient.getMessageList(count, begin_time, end_time);
        return messageListResult;
    }

    /**
     * 获取消息列表
     *
     * @param cursor
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public MessageListResult getMessageListByCursor(String cursor) throws APIConnectionException, APIRequestException {
        MessageListResult messageListResult = jMessageClient.getMessageListByCursor(cursor);
        return messageListResult;
    }

    /**
     * 获取消息列表
     *
     * @param username
     * @param cursor
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public MessageListResult getUserMessagesByCursor(String username, String cursor) throws APIConnectionException, APIRequestException {
        MessageListResult messageListResult = jMessageClient.getUserMessagesByCursor(username, cursor);
        return messageListResult;
    }

    /**
     * 获取消息列表
     *
     * @param username
     * @param count
     * @param begin_time
     * @param end_time
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public MessageListResult getUserMessages(String username, int count, String begin_time, String end_time) throws APIConnectionException, APIRequestException {
        MessageListResult messageListResult = jMessageClient.getUserMessages(username, count, begin_time, end_time);
        return messageListResult;
    }

    /**
     * 发送文字消息
     *
     * @param targetType
     * @param targetId
     * @param formType
     * @param formId
     * @param text
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public SendMessageResult sendTextMessage(String targetType, String targetId, String formType, String formId, String text) throws APIConnectionException, APIRequestException {
        SendMessageResult sendMessageResult = jMessageClient.sendMessage(JGUtil.IM_VERSION, targetType, targetId, formType, formId, MessageType.TEXT, MessageBody.newBuilder().setText(text).build());
        return sendMessageResult;
    }

    /**
     * 发送图片消息
     *
     * @param targetType
     * @param targetId
     * @param formType
     * @param formId
     * @param body
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public SendMessageResult sendImageMessage(String targetType, String targetId, String formType, String formId, JGIMMessage.Body body) throws APIConnectionException, APIRequestException {
        MessageBody build = MessageBody.newBuilder()
                .setMediaId(body.getMedia_id())
                .setMediaCrc32(body.getMedia_crc32())
                .setWidth(body.getWidth())
                .setHeight(body.getHeight())
                .setFsize(body.getFsize())
                .setFormat(body.getFormat())
                .build();
        SendMessageResult sendMessageResult = jMessageClient.sendMessage(JGUtil.IM_VERSION, targetType, targetId, formType, formId, MessageType.IMAGE, build);
        return sendMessageResult;
    }

    /**
     * 发送语音消息
     *
     * @param targetType
     * @param targetId
     * @param formType
     * @param formId
     * @param body
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public SendMessageResult sendVoiceMessage(String targetType, String targetId, String formType, String formId, JGIMMessage.Body body) throws APIConnectionException, APIRequestException {
        MessageBody build = MessageBody.newBuilder()
                .setMediaId(body.getMedia_id())
                .setMediaCrc32(body.getMedia_crc32())
                .setDuration(body.getDuration())
                .setHash(body.getHash())
                .setFsize(body.getFsize())
                .build();
        SendMessageResult sendMessageResult = jMessageClient.sendMessage(JGUtil.IM_VERSION, targetType, targetId, formType, formId, MessageType.VOICE, build);
        return sendMessageResult;
    }

}
