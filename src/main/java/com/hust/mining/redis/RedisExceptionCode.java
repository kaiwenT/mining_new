package com.hust.mining.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis异常代码
 * @ClassName: RedisExceptionCode
 * @Description: redis异常code
 * @author gaoyan
 */
public class RedisExceptionCode {
    public static final String EXTRA_EXCEPTION_MSG_SPLITER = ": ";
    public static Map<Integer, String> EXCEPTION_CODE_MAP;
    public static final int PROJECTNAME_EXCEPTION_CODE_NOT_FOUND = 1;

    static {
        EXCEPTION_CODE_MAP = new HashMap<Integer, String>();
        EXCEPTION_CODE_MAP.put(PROJECTNAME_EXCEPTION_CODE_NOT_FOUND,
                "[PROJECTNAME Exception] Not found exception code.");

    }
}
