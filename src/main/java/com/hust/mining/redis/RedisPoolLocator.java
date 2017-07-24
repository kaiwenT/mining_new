package com.hust.mining.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Title: ComponentLocator
 * </p>
 * <p>
 * Description: 取bean组件的bean
 * </p>
 * 
 * @author gaoyan
 * @version 1.0
 */
@Component(value = "redisPoolLocator")
public class RedisPoolLocator implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName, Class<T> cla) {
        Object obj = applicationContext.getBean(beanName, cla);
        if (obj != null) {
            return (T) obj;
        }
        return null;
    }

    public static <T> T getBean(Class<T> cls) {
        return applicationContext.getBean(cls);
    }

}
