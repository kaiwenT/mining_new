package com.hust.mining.redis;

/**
 * Redis异常处理类.
 * 
 * @author gaoyan
 */
public class RedisException extends Exception {
    /** 自定义serialVersionUID. **/
    private static final long serialVersionUID = -760418183494009606L;
    private int exceptionCode;
    private String detailMsg;

    protected RedisException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public RedisException(int exceptionCode, String extraMsg) {
        super();
        this.setDetailMsg(exceptionCode);
        this.setExtraMsg(extraMsg);
    }

    public RedisException(int exceptionCode) {
        super();
        this.setDetailMsg(exceptionCode);
    }

    public int getExceptionCode() {
        return exceptionCode;
    }

    private void setDetailMsg(int exceptionCode) {
        this.exceptionCode = exceptionCode;
        if (RedisExceptionCode.EXCEPTION_CODE_MAP.containsKey(exceptionCode)) {
            this.detailMsg = RedisExceptionCode.EXCEPTION_CODE_MAP
                    .get(exceptionCode);
        } else {
            this.detailMsg = RedisExceptionCode.EXCEPTION_CODE_MAP
                    .get(RedisExceptionCode.PROJECTNAME_EXCEPTION_CODE_NOT_FOUND);
        }
    }

    private void setExtraMsg(String extraMsg) {
        this.detailMsg += RedisExceptionCode.EXTRA_EXCEPTION_MSG_SPLITER
                + extraMsg;
    }

    @Override
    public String getMessage() {
        return this.detailMsg;
    }
}
