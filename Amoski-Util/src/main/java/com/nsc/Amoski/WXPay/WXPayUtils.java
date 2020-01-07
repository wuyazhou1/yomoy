package com.nsc.Amoski.WXPay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.rmi.ServerException;
import java.util.HashMap;
import java.util.Map;

public class WXPayUtils {

    private static Logger logger = LoggerFactory.getLogger(WXPayUtils.class);

    /**
     * wap h5支付
     * @param request
     * @param out_trade_no 订单号
     * @param body 商品描述
     * @param zcsjfy 金额
     * @return
     */
    public static String getWxpayWapPayStr(HttpServletRequest request, String out_trade_no, String body, String zcsjfy, String returnUrl) throws ServerException {
        String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://yomoy.com.cn\",\"wap_name\": \""+body+"\"}}";
        logger.info(scene_info);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        data.put("out_trade_no", out_trade_no);
        data.put("total_fee", getMoney(zcsjfy));
        data.put("spbill_create_ip", IpUtils.getIpFromRequest(request));
        data.put("notify_url", WXPayConfig.notify_url);
        data.put("trade_type", "MWEB");
        data.put("scene_info", scene_info);
        //data.put("redirect_url", "yomoy.com.cn://");
        logger.info("ip=============================="+IpUtils.getIpFromRequest(request));
        logger.info("data=====>"+JSON.toJSONString(data));
        String form = "";
        try {
            WXPayConfigImpl config = WXPayConfigImpl.getInstance();
            logger.info("config=====>"+JSON.toJSONString(config));
            WXPay wxpay = new WXPay(config);
            Map<String, String> r = wxpay.unifiedOrder(data);
            logger.info("r=====>"+JSON.toJSONString(r));
            logger.info("return_coder=====>"+r.get("return_code"));
            if("SUCCESS".equals(r.get("return_code"))) {
                String mweb_url = r.get("mweb_url")+"yomoy.com.cn://";
                if(StringUtils.isNotBlank(returnUrl)) {
                    mweb_url = r.get("mweb_url")+"&redirect_url="+returnUrl;
                }else {
                    mweb_url = r.get("mweb_url");
                }
                mweb_url = r.get("mweb_url")+
                        "&redirect_url=com.elder.amoski://";
                form = WebUtils.buildForm(mweb_url, r);
            }else {
                throw new ServerException(r.get("return_msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerException(e.getMessage());
        }
        return form;
    }


    /**
     * 微信内公众号h5支付
     * @param request
     * @param out_trade_no 订单号
     * @param body 商品描述
     * @param zcsjfy 金额
     * @param openid
     * @return
     */
    public static Object getWxpayWxWapPayStr(HttpServletRequest request, String out_trade_no, String body, String zcsjfy, String returnUrl, String openid) throws ServerException {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        data.put("openid", openid);
        data.put("out_trade_no", out_trade_no);
        data.put("total_fee", zcsjfy);
        data.put("spbill_create_ip", IpUtils.getIpFromRequest(request));
        data.put("notify_url", WXPayConfig.notify_url);
        data.put("trade_type", "JSAPI");
        System.out.println("ip=============================="+IpUtils.getIpFromRequest(request));
        try {
            WXPayConfigImpl config = WXPayConfigImpl.getInstance();
            WXPay wxpay = new WXPay(config);
            Map<String, String> r = wxpay.unifiedOrder(data);
            System.out.println(r);
            if("SUCCESS".equals(r.get("return_code"))) {
                Map<String, String> map = new HashMap<>();
                map.put("appId", WXPayConfig.appid);
                map.put("timeStamp", String.valueOf(System.currentTimeMillis()/1000));
                map.put("nonceStr", WXPayUtil.generateUUID());
                map.put("package", "prepay_id="+r.get("prepay_id"));
                map.put("signType", WXPayConfig.signType);
                map.put("paySign", WXPayUtil.generateSignature(map, WXPayConfig.Key, WXPayConstants.SignType.HMACSHA256));
                return  map;
            }else {
                throw new ServerException(r.get("return_msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //throw new ServerException(e.getMessage());
            throw new ServerException(e.getMessage());
        }
    }


    /**
     * 元转换成分
     * @param amount
     * @return
     */
    public static String getMoney(String amount) {
        if(amount==null){
            return "";
        }
        // 金额转化为分为单位
        // 处理包含, ￥ 或者$的金额
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if(index == -1){
            amLong = Long.valueOf(currency+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");
        }
        return amLong.toString();
    }


    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        System.out.println(output);
        return output;
    }
}
