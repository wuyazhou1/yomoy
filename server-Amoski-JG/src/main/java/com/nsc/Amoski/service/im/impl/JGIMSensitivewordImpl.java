package com.nsc.Amoski.service.im.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.sensitiveword.SensitiveWordListResult;
import cn.jmessage.api.sensitiveword.SensitiveWordStatusResult;
import com.nsc.Amoski.service.im.JGIMSensitivewordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Service
public class JGIMSensitivewordImpl implements JGIMSensitivewordService {

    @Autowired
    private JMessageClient jMessageClient;

    /**
     * 添加敏感词
     *
     * @param words
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public ResponseWrapper addSensitiveWords(String... words) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.addSensitiveWords(words);
        return responseWrapper;
    }

    /**
     * 删除敏感词
     *
     * @param word
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public ResponseWrapper deleteSensitiveWord(String word) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.deleteSensitiveWord(word);
        return responseWrapper;
    }

    /**
     * 更新敏感词
     *
     * @param newWord
     * @param oldWord
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public ResponseWrapper updateSensitiveWord(String newWord, String oldWord) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.updateSensitiveWord(newWord, oldWord);
        return responseWrapper;
    }

    /**
     * 获取敏感词列表
     *
     * @param start
     * @param count
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public SensitiveWordListResult getSensitiveWordList(int start, int count) throws APIConnectionException, APIRequestException {
        SensitiveWordListResult sensitiveWordList = jMessageClient.getSensitiveWordList(start, count);
        return sensitiveWordList;
    }

    /**
     * 更新敏感词状态
     *
     * @param status
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public ResponseWrapper updateSensitiveWordStatus(int status) throws APIConnectionException, APIRequestException {
        ResponseWrapper responseWrapper = jMessageClient.updateSensitiveWordStatus(status);
        return responseWrapper;
    }

    /**
     * 获取敏感词状态
     *
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
    @Override
    public SensitiveWordStatusResult getSensitiveWordStatus() throws APIConnectionException, APIRequestException {
        SensitiveWordStatusResult sensitiveWordStatusResult = jMessageClient.getSensitiveWordStatus();
        return sensitiveWordStatusResult;
    }
}