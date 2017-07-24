package com.hust.mining.redis;

/**
 * 用于Redis 控制并发的回调函数
 * 
 * @author gaoyan
 */
public interface IRedisCallBack<T> {

    /**
     * 由旧值来计算新值
     * 
     * @param oldValue
     * @return
     */
    public T computeNewValue(T oldValue);
}
