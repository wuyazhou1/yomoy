package com.nsc.Amoski.entity.jg;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 李阳
 * @date 2019/12/13 15:14
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
public enum ReturnCode {

    OK(0, "成功"),
    OWNER_NULL(100, "群主账号不能为空"),
    GNAME_NULL(101, "群名不能为空"),
    GROUPNAME_NULL(102, "群组名不能为空"),
    GROUPDESC_NULL(103, "群组描述不能为空"),
    AVATAR_NULL(104, "群组头像不能为空"),
    ADDLIST_AND_REMOTELIST_NULL(105, "两个参数不能都为空"),
    OLDWORD_NULL(106, "敏感词不能为空"),
    NEWWORD_NULL(107, "新敏感词不能为空"),
    TO_USERNAME_NULL(108, "对方账号不能为空"),
    NEWPASSWORD_NULL(109, "新密码不能为空"),
    IM_VERSION_NO(110, "IM版本号不合法"),
    IM_TARGET_NO(111, "发送目标类型不合法"),
    IM_FORM_TYPE_NO(112, "发送消息者的身份不合法"),
    IM_MSG_TYPE_NO(113, "发消息类型不合法"),
    IM_FROM_ID_NULL(114, "发送者不能为空"),
    IM_TARGET_ID_NULL(115, "接受者不能为空"),
    IM_FILE_TYPE_NO(116, "文件类型不合法"),
    IM_MSG_BODY_NULL(117, "消息体不能为空"),
    IM_REGISTER_USERNAME_NO_NULL(118, "账号已存在"),
    IM_USERNAME_NULL(119, "账号不能为空"),
    NO(9999, "失败");

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}