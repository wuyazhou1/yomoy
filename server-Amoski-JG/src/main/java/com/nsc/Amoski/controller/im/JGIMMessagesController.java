package com.nsc.Amoski.controller.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.message.MessageListResult;
import cn.jmessage.api.message.MessageType;
import cn.jmessage.api.message.SendMessageResult;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.JGIMMessage;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.service.im.JGIMMessagesService;
import com.nsc.Amoski.utlis.JGUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@RestController
@RequestMapping(path = "/imMessages")
@Api(value = "JGIMMessagesController", tags = "极光IM消息接口")
public class JGIMMessagesController {

    @Autowired
    private JGIMMessagesService jgimMessagesService;

    /**
     * 撤回消息
     *
     * @param username
     * @param msgId
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "撤回消息", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "极光IM账号", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "msgId", value = "消息id", required = true, dataType = "long", paramType = "path")
    })
    @PutMapping(path = "/retractMessage/{msgId}")
    public Result retractMessage(@RequestParam String username, @PathVariable long msgId) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jgimMessagesService.retractMessage(username, msgId);
        return new Result(ReturnCode.OK, responseWrapper);
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
    @ApiOperation(value = "获取消息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "count", value = "条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "begin_time", value = "开始时间", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(path = "/getMessageList/{count}/{begin_time}/{end_time}")
    public Result getMessageList(@PathVariable int count, @PathVariable String begin_time, @PathVariable String end_time) throws APIConnectionException, APIRequestException {
        MessageListResult messageListResult = jgimMessagesService.getMessageList(count, begin_time, end_time);
        return new Result(ReturnCode.OK, messageListResult);
    }

    /**
     * 获取消息列表
     *
     * @param cursor
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "获取消息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cursor", value = "游标", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(path = "/getMessageListByCursor/{cursor}")
    public Result getMessageListByCursor(@PathVariable String cursor) throws APIConnectionException, APIRequestException {
        MessageListResult messageListResult = jgimMessagesService.getMessageListByCursor(cursor);
        return new Result(ReturnCode.OK, messageListResult);
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
    @ApiOperation(value = "获取消息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cursor", value = "游标", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(path = "/getMessagesByCursor/{cursor}")
    public Result getUserMessagesByCursor(@RequestParam String username, @PathVariable String cursor) throws APIConnectionException, APIRequestException {
        MessageListResult messagesByCursor = jgimMessagesService.getUserMessagesByCursor(username, cursor);
        return new Result(ReturnCode.OK, messagesByCursor);
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
    @ApiOperation(value = "获取消息列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "count", value = "条数", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "begin_time", value = "开始时间", required = true, dataType = "string", paramType = "path"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", required = true, dataType = "string", paramType = "path")
    })
    @GetMapping(path = "/getUserMessages/{count}/{begin_time}/{end_time}")
    public Result getUserMessages(@RequestParam String username, @PathVariable int count, @PathVariable String begin_time, @PathVariable String end_time) throws APIConnectionException, APIRequestException {
        MessageListResult messageListResult = jgimMessagesService.getUserMessages(username, count, begin_time, end_time);
        return new Result(ReturnCode.OK, messageListResult);
    }

    /**
     * 发送消息
     *
     * @param jgimMessage
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @ApiOperation(value = "发送消息", httpMethod = "POST")
    @PostMapping(path = "/sendMessage")
    public Result sendMessage(@RequestBody JGIMMessage jgimMessage) throws APIConnectionException, APIRequestException {
        if (StringUtils.isEmpty(jgimMessage.getVersion()) || !JGUtil.IM_VERSION.equals(jgimMessage.getVersion())) {
            return new Result(ReturnCode.IM_VERSION_NO);
        }
        if (StringUtils.isEmpty(jgimMessage.getTarget_type()) || (!JGUtil.IM_TARGET_SINGLE.equals(jgimMessage.getTarget_type()) && !JGUtil.IM_TARGET_GROUP.equals(jgimMessage.getTarget_type()) && !JGUtil.IM_TARGET_CHATROOM.equals(jgimMessage.getTarget_type()))) {
            return new Result(ReturnCode.IM_TARGET_NO);
        }
        if (StringUtils.isEmpty(jgimMessage.getFrom_type()) || (!JGUtil.IM_FORM_TYPE_ADMIN.equals(jgimMessage.getFrom_type()) && !JGUtil.IM_FORM_TYPE_USER.equals(jgimMessage.getFrom_type()))) {
            return new Result(ReturnCode.IM_FORM_TYPE_NO);
        }
        if (StringUtils.isEmpty(jgimMessage.getMsg_type()) || (!JGUtil.IM_MSG_TYPE_TEXT.equals(jgimMessage.getMsg_type()) && !JGUtil.IM_MSG_TYPE_IMAGE.equals(jgimMessage.getMsg_type()) && !JGUtil.IM_MSG_TYPE_VOICE.equals(jgimMessage.getMsg_type()))) {
            return new Result(ReturnCode.IM_MSG_TYPE_NO);
        }
        if (StringUtils.isEmpty(jgimMessage.getFrom_id())) {
            return new Result(ReturnCode.IM_FROM_ID_NULL);
        }
        if (StringUtils.isEmpty(jgimMessage.getTarget_id())) {
            return new Result(ReturnCode.IM_TARGET_ID_NULL);
        }
        SendMessageResult sendMessageResult = null;
        JGIMMessage.Body body = jgimMessage.getBody();
        if (null == body) {
            return new Result(ReturnCode.IM_MSG_BODY_NULL);
        }
        if (jgimMessage.getMsg_type().equals(MessageType.TEXT.getValue())) {
            sendMessageResult = jgimMessagesService.sendTextMessage(jgimMessage.getTarget_type(), jgimMessage.getTarget_id(), jgimMessage.getFrom_type(), jgimMessage.getFrom_id(), jgimMessage.getBody().getText());
        }
        if (jgimMessage.getMsg_type().equals(MessageType.IMAGE.getValue())) {
            sendMessageResult = jgimMessagesService.sendImageMessage(jgimMessage.getTarget_type(), jgimMessage.getTarget_id(), jgimMessage.getFrom_type(), jgimMessage.getFrom_id(), body);
        }
        if (jgimMessage.getMsg_type().equals(MessageType.VOICE.getValue())) {
            sendMessageResult = jgimMessagesService.sendVoiceMessage(jgimMessage.getTarget_type(), jgimMessage.getTarget_id(), jgimMessage.getFrom_type(), jgimMessage.getFrom_id(), body);
        }
        return new Result(ReturnCode.OK, sendMessageResult);
    }

}
