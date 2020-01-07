package com.nsc.Amoski.AlipayPay;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 支付宝支付工具类
 */
public class AlipayPayUtils {
    private Logger logger = LoggerFactory.getLogger(AlipayPayUtils.class);

    /**
     * 支付宝支付
     * @param body 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
     * @param subject 订单名称，必填
     * @param out_trade_no 商户订单号，商户网站订单系统中唯一订单号，必填
     * @param timeout_express 该笔订单允许的最晚付款时间，m-分钟，h-小时，d-天，1c-当天
     * @param total_amount 付款金额，必填
     * @return
     * @throws AlipayApiException
     */
    public Object AlipayPay(String body, String subject, String out_trade_no, String timeout_express,
                            String total_amount, HttpServletResponse response) throws AlipayApiException {
        logger.info("body=="+body+";subject=="+subject+";out_trade_no=="+out_trade_no+";" +
                "timeout_express=="+timeout_express+";total_amount==>"+total_amount);
        // 销售产品码 必填
        String product_code = "QUICK_WAP_WAY";
        // 实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayPayConfig.URL, AlipayPayConfig.APPID,
                AlipayPayConfig.RSA_PRIVATE_KEY, AlipayPayConfig.FORMAT, AlipayPayConfig.CHARSET,
                AlipayPayConfig.ALIPAY_PUBLIC_KEY, AlipayPayConfig.SIGNTYPE);
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
        model.setSubject(subject);//商品的标题/交易标题/订单标题/订单关键字等
        model.setOutTradeNo(out_trade_no);//商户网站唯一订单号
        model.setTimeoutExpress(timeout_express);//该笔订单允许的最晚付款时间，m-分钟，h-小时，d-天，1c-当天
        model.setTotalAmount(total_amount);//订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        model.setProductCode(product_code);//销售产品码，商家和支付宝签约的产品码。该产品请填写固定值：
        alipayRequest.setBizModel(model);
        // 设置异步通知地址
        alipayRequest.setNotifyUrl(AlipayPayConfig.notify_url);
        // 设置同步地址
        alipayRequest.setReturnUrl(AlipayPayConfig.return_url);
        logger.info("签名信息：["+out_trade_no+"]"+ JSON.toJSONString(alipayRequest)+"\n\n");
        // 这里和普通的接口调用不同，使用的是sdkExecute
        //AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        //String form = alipayClient.pageExecute(alipayRequest).getBody();

        /*AlipayTradeAppPayResponse alipayResponse = alipayClient.sdkExecute(alipayRequest);
        logger.info(alipayResponse.getBody()+"\n\n");// 就是orderString 可以直接给客户端请求，无需再做处理。
        logger.info("获取签名：["+out_trade_no+"]"+alipayResponse.getBody()+"\n\n");*/
        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
            logger.info("for==>"+form.toString());
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return alipayResponse.getBody();
        return form;
    }



    /**
     * 支付宝支付
     * @param body 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
     * @param subject 订单名称，必填
     * @param out_trade_no 商户订单号，商户网站订单系统中唯一订单号，必填
     * @param timeout_express 该笔订单允许的最晚付款时间，m-分钟，h-小时，d-天，1c-当天
     * @param total_amount 付款金额，必填
     * @return
     * @throws AlipayApiException
     */
    public Object AlipayPay1(String body, String subject, String out_trade_no, String timeout_express,
                            String total_amount, HttpServletResponse response) throws AlipayApiException {
        logger.info("body=="+body+";subject=="+subject+";out_trade_no=="+out_trade_no+";" +
                "timeout_express=="+timeout_express+";total_amount==>"+total_amount);
        // 销售产品码 必填
        String product_code = "QUICK_WAP_WAY";
        // 实例化客户端
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(AlipayPayConfig.URL);
        certAlipayRequest.setAppId(AlipayPayConfig.APPID);
        certAlipayRequest.setPrivateKey(AlipayPayConfig.RSA_PRIVATE_KEY);//应用私钥
        certAlipayRequest.setFormat(AlipayPayConfig.FORMAT);
        certAlipayRequest.setCharset(AlipayPayConfig.CHARSET);
        certAlipayRequest.setSignType(AlipayPayConfig.SIGNTYPE);
        certAlipayRequest.setCertPath(AlipayPayConfig.ALIPAY_PUBLIC_KEY);//应用公钥证书文件路径
        certAlipayRequest.setAlipayPublicCertPath(AlipayPayConfig.ALIPAY_CERT_PATH);//支付宝公钥证书文件路径
        certAlipayRequest.setRootCertPath(AlipayPayConfig.ALIPAY_ROOT_CERT_PATH);//支付宝CA根证书文件路径
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        // SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);//对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
        model.setSubject(subject);//商品的标题/交易标题/订单标题/订单关键字等
        model.setOutTradeNo(out_trade_no);//商户网站唯一订单号
        model.setTimeoutExpress(timeout_express);//该笔订单允许的最晚付款时间，m-分钟，h-小时，d-天，1c-当天
        model.setTotalAmount(total_amount);//订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        model.setProductCode(product_code);//销售产品码，商家和支付宝签约的产品码。该产品请填写固定值：
        alipayRequest.setBizModel(model);
        alipayRequest.setNotifyUrl(AlipayPayConfig.notify_url);
        // 设置异步通知地址
        //alipayRequest.setNotifyUrl(AlipayPayConfig.notify_url);
        // 设置同步地址
        alipayRequest.setReturnUrl(AlipayPayConfig.return_url);
        logger.info("签名信息：["+out_trade_no+"]"+ JSON.toJSONString(alipayRequest)+"\n\n");
        // 这里和普通的接口调用不同，使用的是sdkExecute
        //AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        //String form = alipayClient.pageExecute(alipayRequest).getBody();

        /*AlipayTradeAppPayResponse alipayResponse = alipayClient.sdkExecute(alipayRequest);
        logger.info(alipayResponse.getBody()+"\n\n");// 就是orderString 可以直接给客户端请求，无需再做处理。
        logger.info("获取签名：["+out_trade_no+"]"+alipayResponse.getBody()+"\n\n");*/
        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
            logger.info("for==>"+form.toString());
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return alipayResponse.getBody();
        return form;
    }

    /**
     * 支付宝订单查询
     * @param out_trade_no 商户订单号，商户网站订单系统中唯一订单号，必填
     * @return
     * @throws AlipayApiException
     */
    public Object AlipayPayQuery(String out_trade_no) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayPayConfig.URL,AlipayPayConfig.APPID,
                AlipayPayConfig.RSA_PRIVATE_KEY,
                AlipayPayConfig.FORMAT, AlipayPayConfig.CHARSET,AlipayPayConfig.ALIPAY_PUBLIC_KEY, AlipayPayConfig.SIGNTYPE);
        AlipayTradeQueryRequest rq = new AlipayTradeQueryRequest();
        rq.setBizContent("{" +
                "\"out_trade_no\":\""+out_trade_no+"\""+////商户网站唯一订单号
							/*+ "  ," +
					"\"trade_no\":\"2014112611001004680073956707\"" +*/
                "}");
        AlipayTradeQueryResponse rp = alipayClient.execute(rq);
        if(rp.isSuccess()){
            logger.info("调用成功"+rp.getBody());
            ErrorEnum.ErrorUtil errorEnumByCode = ErrorEnum.ErrorUtil.getErrorEnumByCode(rp.getTradeStatus());
            return errorEnumByCode;
        } else {
            logger.info("调用失败"+rp.getBody());
            ErrorEnum.ErrorUtil errorEnumByCode = ErrorEnum.ErrorUtil.getErrorEnumByCode(rp.getTradeStatus());
            return errorEnumByCode;
        }
    }

    /**
     * 支付宝退款
     * @param out_trade_no
     * @param refund_amount
     * @return
     * @throws AlipayApiException
     */
    public Object AlipayRefund(String out_trade_no,String refund_amount) throws AlipayApiException {
        //AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayPayConfig.URL,AlipayPayConfig.APPID,
                AlipayPayConfig.RSA_PRIVATE_KEY,
                AlipayPayConfig.FORMAT, AlipayPayConfig.CHARSET,AlipayPayConfig.ALIPAY_PUBLIC_KEY, AlipayPayConfig.SIGNTYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        logger.info("订单号:"+ out_trade_no);
        logger.info("退款金额:"+ refund_amount);
        request.setBizContent("{" +
                "\"out_trade_no\":\""+out_trade_no+"\"," + //订单支付时传入的商户订单号,不能和 trade_no同时为空。
                "\"refund_amount\":"+refund_amount+"," + //需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
                "  }");
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            logger.info("调用成功:"+ response.getBody());
            return "调用成功";
        } else {
            throw new RuntimeException("退款失败");
            //logger.info("调用失败"+response.getBody());
            //return "调用成功";
        }
    }

    /**
     * 支付宝退款
     * @param out_trade_no
     * @param refund_amount
     * @return
     * @throws AlipayApiException
     */
    public Object AlipayRefund1(String out_trade_no, String refund_amount) throws AlipayApiException, RuntimeException {


        logger.info("\nAlipayPayConfig.URL:"+ AlipayPayConfig.URL);
        logger.info("\nAlipayPayConfig.APPID:"+ AlipayPayConfig.APPID);
        logger.info("\nAlipayPayConfig.RSA_PRIVATE_KEY:"+ AlipayPayConfig.RSA_PRIVATE_KEY);
        logger.info("\nAlipayPayConfig.FORMAT:"+ AlipayPayConfig.FORMAT);
        logger.info("\nAlipayPayConfig.CHARSET:"+ AlipayPayConfig.CHARSET);
        logger.info("\nAlipayPayConfig.SIGNTYPE:"+ AlipayPayConfig.SIGNTYPE);
        logger.info("\nAlipayPayConfig.ALIPAY_PUBLIC_KEY:"+ AlipayPayConfig.ALIPAY_PUBLIC_KEY);
        logger.info("\nAlipayPayConfig.ALIPAY_CERT_PATH:"+ AlipayPayConfig.ALIPAY_CERT_PATH);
        logger.info("\nAlipayPayConfig.ALIPAY_ROOT_CERT_PATH:"+ AlipayPayConfig.ALIPAY_ROOT_CERT_PATH);


        // 实例化客户端
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
        certAlipayRequest.setServerUrl(AlipayPayConfig.URL);
        certAlipayRequest.setAppId(AlipayPayConfig.APPID);
        certAlipayRequest.setPrivateKey(AlipayPayConfig.RSA_PRIVATE_KEY);//应用私钥
        certAlipayRequest.setFormat(AlipayPayConfig.FORMAT);
        certAlipayRequest.setCharset(AlipayPayConfig.CHARSET);
        certAlipayRequest.setSignType(AlipayPayConfig.SIGNTYPE);
        certAlipayRequest.setCertPath(AlipayPayConfig.ALIPAY_PUBLIC_KEY);//应用公钥证书文件路径
        certAlipayRequest.setAlipayPublicCertPath(AlipayPayConfig.ALIPAY_CERT_PATH);//支付宝公钥证书文件路径
        certAlipayRequest.setRootCertPath(AlipayPayConfig.ALIPAY_ROOT_CERT_PATH);//支付宝CA根证书文件路径
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
        // 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        logger.info("\n订单号:"+ out_trade_no);
        logger.info("\n退款金额:"+ refund_amount);
        request.setBizContent("{" +
                "\"out_trade_no\":\""+out_trade_no+"\"," + //订单支付时传入的商户订单号,不能和 trade_no同时为空。
                "\"refund_amount\":"+refund_amount+"" + //需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
                "  }");
        AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
        if(response.isSuccess()){
            logger.info("\n调用成功:"+ response.getBody());
            return "调用成功";
        } else {
            logger.info("\n调用失败"+response.getBody());
            throw new RuntimeException("退款失败");
            //return "调用失败";
        }
    }

}
