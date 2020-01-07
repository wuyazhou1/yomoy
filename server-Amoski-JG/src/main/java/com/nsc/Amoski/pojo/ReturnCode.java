package com.nsc.Amoski.pojo;

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
    USERNAME_NULL(102, "账号不能为空"),
    PASSWORD_NULL(103, "密码不能为空"),
    GROUPNAME_NULL(104, "群组名不能为空"),
    GROUPDESC_NULL(105, "群组描述不能为空"),
    AVATAR_NULL(106, "群组头像不能为空"),
    ADDLIST_AND_REMOTELIST_NULL(107, "两个参数不能都为空"),
    OLDWORD_NULL(108, "敏感词不能为空"),
    NEWWORD_NULL(109, "新敏感词不能为空"),
    TO_USERNAME_NULL(110, "对方账号不能为空"),
    NEWPASSWORD_NULL(111, "新密码不能为空"),
    IM_VERSION_NO(112, "IM版本号不合法"),
    IM_TARGET_NO(113, "发送目标类型不合法"),
    IM_FORM_TYPE_NO(114, "发送消息者的身份不合法"),
    IM_MSG_TYPE_NO(115, "发消息类型不合法"),
    IM_FROM_ID_NULL(116, "发送者不能为空"),
    IM_TARGET_ID_NULL(117, "接受者不能为空"),
    IM_FILE_TYPE_NO(118, "文件类型不合法"),
    IM_MSG_BODY_NULL(119, "消息体不能为空"),
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