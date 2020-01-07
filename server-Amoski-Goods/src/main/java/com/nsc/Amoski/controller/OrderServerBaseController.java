package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.BaseMsg;
import com.nsc.Amoski.dto.CommMsg;
import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.repository.BaseController;
import com.nsc.Amoski.uti.ResultUtil;
import com.nsc.Amoski.util.GsonUtil;
import com.nsc.Amoski.util.RegUtil;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderServerBaseController<T> extends BaseController {
    //打印日志对象
    Logger log;

    public RegUtil regUtil= RegUtil.getSingleton();

    @SuppressWarnings("unchecked")
    public OrderServerBaseController() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        log= LoggerFactory.getLogger((Class<T>) type.getActualTypeArguments()[0]);
        //System.out.println("Dao实现类是：" + entityClass.getName());
    }

    public Result success(){
        Result result = ResultUtil.success();
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }
    public <T> Result success(T t){
        Result result = ResultUtil.success(t);
        log.info("===============responseData:"+ GsonUtil.dtoToJson(result));
        return result;
    }
    public Result error(String code,String msg){
        Result result = ResultUtil.error(code,msg);
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }

    public Result error(ResultMsg msg){
        Result result = ResultUtil.error(msg);
        log.info("===============responseData:"+ GsonUtil.dtoToJson(result));
        return result;
    }
    public <T> Result error(ResultMsg msg,T t){
        Result result = ResultUtil.error(msg,t);
        log.info("===============responseData:"+GsonUtil.dtoToJson(result));
        return result;
    }


    /**
     * 分发不同消息类型
     */
    public static String dealDifferMsgType(Map<String,String> xmlData){

        //获取 消息类型
        String msgType = xmlData.get("MsgType");
        BaseMsg baseMsg = null;
        //普通文本消息
        if ("text".equals(msgType)) {
            baseMsg = dealText(xmlData);

            //图片消息
        } else if ("image".equals(msgType)) {//语音消息

        } else if ("voice".equals(msgType)) {//视频消息

        } else if ("video".equals(msgType)) {//小视频消息

        } else if ("shortvideo".equals(msgType)) {//地理位置消息

        } else if ("location".equals(msgType)) {//链接消息

        } else if ("link".equals(msgType)) {

        } else if ("event".equals(msgType)) {
            baseMsg = dealDifferEventMsgType(xmlData);
        }
        //把消息对象处理为xml数据包
        String xml = bean2Xml(baseMsg);
        if(xml!=null){
            return xml;
        }
        return null;
    }

    /**
     * 分发不同事件消息类型
     */
    public static BaseMsg dealDifferEventMsgType(Map<String,String> xmlData){

        //获取 消息类型
        String event = xmlData.get("Event");
        BaseMsg baseMsg = null;
        if ("click".equals(event)) {

        } else if ("subscribe".equals(event) || "unsubscribe".equals(event) || "scan".equals(event)) {
        } else if ("location".equals(event)) {
        }
        return baseMsg;
    }

    /**
     * 处理文本消息
     * @param xmlData
     * @return TextMsg
     */
    private static BaseMsg dealText(Map<String, String> xmlData) {
        //获取请求聊天信息
        String content = xmlData.get("Content");
        //回复文本消息
        BaseMsg bm = new CommMsg(xmlData,"欢迎访问公众号哟！");
        return bm;
    }

    /**
     * 解析xml 获取key-value
     * @param inputStream
     * @return
     */
    public static Map<String,String> getXmlData(InputStream inputStream){
        Map<String,String> map =  new HashMap<String, String>();
        //截取xml
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(inputStream);
            Element rootElement = document.getRootElement(); //获取根节点
            List<Element> elements = rootElement.elements(); // h获取所有的节点
            for (Element e: elements) {
                map.put(e.getName(),e.getStringValue());
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     *  javaBean 转xml
     * @param baseMsg
     * @return  xml
     */
    public static String bean2Xml(BaseMsg baseMsg){

        XStream xStream = new XStream();
        //若没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
        xStream.processAnnotations(BaseMsg.class);
        xStream.processAnnotations(CommMsg.class);
        String xml = xStream.toXML(baseMsg);
        return xml;
    }
}
