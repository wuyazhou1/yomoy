package com.nsc.Amoski.AlipayPay;

import lombok.Data;

@Data
public class ErrorEnum {

    public enum ErrorUtil{

        AmoskiSystemError("AmoskiSystemError","系统发现未知错误","系统发现未知错误"),
        WAIT_BUYER_PAY("WAIT_BUYER_PAY","交易创建","等待买家付款"),
        TRADE_CLOSED("TRADE_CLOSED","未付款交易超时关闭","或支付完成后全额退款"),
        TRADE_SUCCESS("TRADE_SUCCESS","交易支付成功","请刷新页面"),
        SYSTEM_ERROR("SYSTEM_ERROR","系统错误","重新发起请求"),
        INVALID_PARAMETER("INVALID_PARAMETER","检查请求参数","修改后重新发起请求"),
        TRADE_NOT_EXIST("TRADE_NOT_EXIST","查询的交易不存在","检查传入的交易号是否正确，修改后重新发起请求")
        ;
        public static ErrorUtil getErrorEnumByCode(String code) {
            for (ErrorUtil ms : ErrorUtil.values()) {
                if (ms.getCode().equals(code)) {
                    return ms;
                }
            }
            return ErrorUtil.AmoskiSystemError;
        }

        private ErrorUtil(String code,String title,String message){
            this.code=code;
            this.title=title;
            this.message=message;
        }

        private String code;
        private String title;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
