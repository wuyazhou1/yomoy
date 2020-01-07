/**
 * Copyright 2017 Inc.
 **/
package com.nsc.Amoski.chlientTest;

import java.util.TreeSet;

/**
 * @author zhangshaoyong
 */
public interface IRedisOperator {
  /**
   * 根据pattern 获取所有的keys
   * @param pattern
   * @return
   */
  TreeSet<String> keys(String pattern) throws Exception;

  /**
   * 删除所有的keys
   */
  void flushDB()throws Exception;
}
