package com.nsc.Amoski.service.im;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jmessage.api.sensitiveword.SensitiveWordListResult;
import cn.jmessage.api.sensitiveword.SensitiveWordStatusResult;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
public interface JGIMSensitivewordService {

    ResponseWrapper addSensitiveWords(String... words) throws APIConnectionException, APIRequestException;

    ResponseWrapper deleteSensitiveWord(String words) throws APIConnectionException, APIRequestException;

    ResponseWrapper updateSensitiveWord(String newWord, String oldWord) throws APIConnectionException, APIRequestException;

    SensitiveWordListResult getSensitiveWordList(int start, int count) throws APIConnectionException, APIRequestException;

    ResponseWrapper updateSensitiveWordStatus(int status) throws APIConnectionException, APIRequestException;

    SensitiveWordStatusResult getSensitiveWordStatus() throws APIConnectionException, APIRequestException;

}