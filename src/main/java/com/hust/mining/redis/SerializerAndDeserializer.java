package com.hust.mining.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 序列化/反序列化 -- 服务于redis
 * 
 * @author gaoyan
 */
public class SerializerAndDeserializer {

    private static Logger LOGGER = LoggerFactory.getLogger(SerializerAndDeserializer.class);

    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            return baos.toByteArray();
        } catch (Exception e) {
            LOGGER.error("redis对象存储，对象序列化失败", e);
        }
        return null;
    }

    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);

            return ois.readObject();
        } catch (Exception e) {
            LOGGER.error("redis对象存储，对象反序列化失败", e);
        }
        return null;
    }
}
