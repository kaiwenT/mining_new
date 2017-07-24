package com.hust.mining.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

/**
 * Redis门面--提供统一的Redis操作服务
 * <p/>
 * 针对Redis的操作最好都统一走这里
 *
 * @author gaoyan
 */
public class RedisFacade {

    private static Logger LOGGER = LoggerFactory.getLogger(RedisFacade.class);

    private static final String CHARSET = "UTF-8";

    private JedisPool masterJedisPool;
    private static RedisFacade master1Instance;
    private static RedisFacade master2Instance;
    private static RedisFacade master3Instance;
    private static RedisFacade master4Instance;
    private static RedisFacade master5Instance;
    private static volatile int polling = 1;

    private static final Object LOCK = new Object();

    protected RedisFacade() {
        super();
        // TODO Auto-generated constructor stub
    }

    private RedisFacade(boolean isMaster) {
        if (polling == 1) {
            if (masterJedisPool == null) {
                masterJedisPool = (JedisPool) RedisPoolLocator.getBean("master1Instance");
            }
        } else if (polling == 2) {
            if (masterJedisPool == null) {
                masterJedisPool = (JedisPool) RedisPoolLocator.getBean("master2Instance");
            }
        } else if (polling == 3) {
            if (masterJedisPool == null) {
                masterJedisPool = (JedisPool) RedisPoolLocator.getBean("master3Instance");
            }
        } else if (polling == 4) {
            if (masterJedisPool == null) {
                masterJedisPool = (JedisPool) RedisPoolLocator.getBean("master4Instance");
            }
        } else {
            if (masterJedisPool == null) {
                masterJedisPool = (JedisPool) RedisPoolLocator.getBean("master5Instance");
            }
        }

    }

    public static RedisFacade getInstance(boolean isMaster) {
        if (polling == 1) {
            if (master1Instance == null) {
                synchronized (LOCK) {
                    master1Instance = new RedisFacade(isMaster);
                }
            }
            polling = 2;
            return master1Instance;
        } else if (polling == 2) {
            if (master2Instance == null) {
                synchronized (LOCK) {
                    master2Instance = new RedisFacade(isMaster);
                }
            }
            polling = 3;
            return master2Instance;
        } else if (polling == 3) {
            if (master3Instance == null) {
                synchronized (LOCK) {
                    master3Instance = new RedisFacade(isMaster);
                }
            }
            polling = 4;
            return master3Instance;
        } else if (polling == 4) {
            if (master4Instance == null) {
                synchronized (LOCK) {
                    master4Instance = new RedisFacade(isMaster);
                }
            }
            polling = 5;
            return master4Instance;
        } else {
            if (master5Instance == null) {
                synchronized (LOCK) {
                    master5Instance = new RedisFacade(isMaster);
                }
            }
            polling = 1;
            return master5Instance;
        }

    }

    /**
     * 设置String型缓存值
     *
     * @param key
     * @param value
     */
    public void setStringT(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return;
            }
            byte[] realKey = key.getBytes(CHARSET);
            byte[] realValue = value.getBytes(CHARSET);
            jedis.set(realKey, realValue);
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis 设值失败key=" + key + " value=" + value, ex);
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }

    }

    /**
     * 获得String类型缓存值
     *
     * @param key
     * @return String
     */
    public String getStringT(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            //System.out.println("port:" + jedis.info().substring(252, 265));
            byte[] realKey = key.getBytes(CHARSET);
            byte[] valueByteArray = jedis.get(realKey);
            if (!ArrayUtils.isEmpty(valueByteArray)) {
                return new String(valueByteArray, "UTF-8");
            } else {
                return null;
            }
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis 取值失败key=" + key, ex);
            return null;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
    }

    /**
     * 设置String型缓存值
     *
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        this.setRedisString(key, value);
    }

    public void setRedisString(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return;
            }
            byte[] realKey = key.getBytes(CHARSET);
            byte[] realValue = value.getBytes(CHARSET);
            jedis.set(realKey, realValue);
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis 设值失败key=" + key + " value=" + value, ex);
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
    }

    /**
     * 获得String类型缓存值
     *
     * @param key
     * @return String
     */
    public String getString(String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        }
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            byte[] realKey = key.getBytes(CHARSET);
            byte[] valueByteArray = jedis.get(realKey);
            if (!ArrayUtils.isEmpty(valueByteArray)) {
                return new String(valueByteArray, "UTF-8");
            } else {
                return null;
            }
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis 取值失败key=" + key, ex);
            return "abnormal";
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
    }

    public Boolean exists(String key) throws RedisException {
        Boolean result = false;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.exists(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
            throw new RedisException(1, " redis exists exception !" + key);
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String type(String key) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.type(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    /**
     * 在某段时间后失效
     */
    public Long expire(String key, int seconds) {
        Long result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.expire(key, seconds);

        } catch (Exception e) {
            LOGGER.error("Redis 设值失败key=" + key + " seconds=" + seconds + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    /**
     * 在某个时间点失效
     */
    public Long expireAt(String key, long time) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.expireAt(key, time);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long ttl(String key) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.ttl(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public boolean setbit(String key, long offset, boolean value) {

        Jedis jedis = masterJedisPool.getResource();
        boolean result = false;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.setbit(key, offset, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public boolean getbit(String key, long offset) {
        Jedis jedis = masterJedisPool.getResource();
        boolean result = false;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;

        try {
            result = jedis.getbit(key, offset);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public long setrange(String key, long offset, String value) {
        Jedis jedis = masterJedisPool.getResource();
        long result = 0;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.setrange(key, offset, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String getrange(String key, long startOffset, long endOffset) {
        Jedis jedis = masterJedisPool.getResource();
        String result = null;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.getrange(key, startOffset, endOffset);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String getSet(String key, String value) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long setnx(String key, String value) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String setex(String key, int seconds, String value) {
        String result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            LOGGER.error("Redis 设值失败key=" + key + " value=" + value + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long decrBy(String key, long integer) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.decrBy(key, integer);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long decr(String key) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.decr(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long incrBy(String key, long integer) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.incrBy(key, integer);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long incr(String key) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.incr(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long append(String key, String value) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.append(key, value);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String substr(String key, int start, int end) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.substr(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long hset(String key, String field, String value) {
        Long result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.hset(key, field, value);

        } catch (Exception e) {
            LOGGER.error("Redis设值失败key=" + key + "field=" + field + "value=" + value + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String hget(String key, String field) {
        String result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.hget(key, field);

        } catch (Exception e) {
            LOGGER.error("Redis 设值失败key=" + key + "field=" + field + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long hsetnx(String key, String field, String value) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.hsetnx(key, field, value);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String hmset(String key, Map<String, String> hash) {
        String result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.hmset(key, hash);

        } catch (Exception e) {
            LOGGER.error("Redis 设值失败key=" + key + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public List<String> hmget(String key, String...fields) {
        List<String> result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.hmget(key, fields);

        } catch (Exception e) {
            LOGGER.error("Redis 设值失败key=" + key + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long hincrBy(String key, String field, long value) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.hincrBy(key, field, value);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Boolean hexists(String key, String field) {
        Boolean result = false;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.hexists(key, field);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long del(String key) {
        Long result = null;
        result = this.delRedis(key);
        return result;
    }

    public Long delRedis(String key) {
        Long result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.del(key);

        } catch (Exception e) {
            LOGGER.error("Redis 设值失败key=" + key + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long hdel(String key, String field) {
        Long result = null;
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return result;
            }
            result = jedis.hdel(key, field);

        } catch (Exception e) {
            LOGGER.error("Redis 设值失败key=" + key + e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long hlen(String key) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.hlen(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> hkeys(String key) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.hkeys(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public List<String> hvals(String key) {
        List<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.hvals(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Map<String, String> hgetAll(String key) {
        Map<String, String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.hgetAll(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    /**
     * 在redis list尾部增加一个String
     */
    public Long rpush(String key, String string) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.rpush(key, string);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    /**
     * 在redis list头部增加一个String
     */
    public Long lpush(String key, String string) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.lpush(key, string);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long llen(String key) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.llen(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public List<String> lrange(String key, long start, long end) {
        List<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.lrange(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String ltrim(String key, long start, long end) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.ltrim(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String lIndex(String key, long index) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.lindex(key, index);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String lset(String key, long index, String value) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.lset(key, index, value);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long lrem(String key, long count, String value) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.lrem(key, count, value);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    /**
     * 从redis list头部取出一个key
     */
    public String lpop(String key) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.lpop(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    /**
     * 从redis list尾部取出一个key
     */
    public String rpop(String key) {
        String result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.rpop(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long sadd(String key, String member) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.sadd(key, member);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long sadd(String key, String...member) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.sadd(key, member);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> smembers(String key) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.smembers(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long srem(String key, String member) {
        Jedis jedis = masterJedisPool.getResource();

        Long result = null;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.srem(key, member);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String spop(String key) {
        Jedis jedis = masterJedisPool.getResource();
        String result = null;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.spop(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long scard(String key) {
        Jedis jedis = masterJedisPool.getResource();
        Long result = null;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.scard(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Boolean sismember(String key, String member) {
        Jedis jedis = masterJedisPool.getResource();
        Boolean result = null;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.sismember(key, member);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public String srandmember(String key) {
        Jedis jedis = masterJedisPool.getResource();
        String result = null;
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.srandmember(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zadd(String key, double score, String member) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.zadd(key, score, member);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> zrange(String key, int start, int end) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.zrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zrem(String key, String member) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {
            result = jedis.zrem(key, member);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Double zincrby(String key, double score, String member) {
        Double result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zincrby(key, score, member);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zrank(String key, String member) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrank(key, member);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zrevrank(String key, String member) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrevrank(key, member);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrevrange(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<Tuple> zrangeWithScores(String key, int start, int end) {
        Set<Tuple> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrangeWithScores(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
        Set<Tuple> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrevrangeWithScores(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zcard(String key) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zcard(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Double zscore(String key, String member) {
        Double result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zscore(key, member);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public List<String> sort(String key) {
        List<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.sort(key);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public List<String> sort(String key, SortingParams sortingParameters) {
        List<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.sort(key, sortingParameters);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zcount(String key, double min, double max) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zcount(key, min, max);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, double min, double max) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrangeByScore(key, min, max);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrangeByScore(key, min, max, offset, count);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrevrangeByScore(key, max, min);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Set<String> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrevrangeByScore(key, max, min, offset, count);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Set<Tuple> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrangeByScoreWithScores(key, min, max);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Set<Tuple> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrangeByScoreWithScores(key, min, max, offset, count);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        Set<Tuple> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrevrangeByScoreWithScores(key, max, min);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Set<Tuple> result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zremrangeByRank(String key, int start, int end) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zremrangeByRank(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long zremrangeByScore(String key, double start, double end) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.zremrangeByScore(key, start, end);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        Long result = null;
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return result;
        }
        boolean flag = false;
        try {

            result = jedis.linsert(key, where, pivot, value);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            flag = true;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
        return result;
    }

    /**
     * 设置对象型缓存值
     *
     * @param key
     * @param value
     */
    public void setObject(String key, Object value) {
        if (StringUtils.isBlank(key) || value == null) {
            return;
        }
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return;
        }
        boolean flag = false;
        try {
            byte[] realKey = key.getBytes(CHARSET);
            byte[] realValue = SerializerAndDeserializer.serialize(value);
            jedis.set(realKey, realValue);
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis 设值失败", ex);
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }

    }

    /**
     * 获取对象型缓存值
     *
     * @param key
     * @return Object
     */
    public Object getObject(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return null;
        }
        boolean flag = false;
        try {
            byte[] realKey = key.getBytes(CHARSET);
            byte[] valueByteArray = jedis.get(realKey);
            if (!ArrayUtils.isEmpty(valueByteArray)) {
                return SerializerAndDeserializer.unserialize(valueByteArray);
            } else {
                return null;
            }
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis 取值失败", ex);
            return null;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
    }

    public void delete(String key) {
        this.deleteRedis(key);
    }

    public void deleteRedis(String key) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return;
            }
            byte[] realKey = key.getBytes(CHARSET);
            jedis.del(realKey);
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis 删除失败key=" + key, ex);
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
    }

    /**
     * 跨JVM的线程安全的对象类型设值操作
     * <p/>
     * 支持使用CallBack进行 获取到旧缓存值后做一些业务并产生新值，再将新值加入到缓存
     *
     * @param key
     * @param redisCallBack --- 获取当前的缓存值并做业务产生新缓存值
     */
    @SuppressWarnings("unchecked")
    public <T> void setWithThreadSafe(String key, IRedisCallBack<T> redisCallBack) {
        LOGGER.info("Redis线程安全的设值操作开始，KEY值为：[" + key + "]");

        byte[] realKey = key.getBytes();

        Jedis jedis = masterJedisPool.getResource();
        if (jedis == null) {
            return;
        }
        boolean flag = false;
        try {

            while (true) {
                // 监听Key，在后续事务过程中，如果其他进程修改了key的value值，则后续事务失败，transaction.exec返回null
                jedis.watch(realKey);

                try {
                    byte[] oldValueBypeArray = jedis.get(realKey);

                    T oldValue = null;

                    if (!ArrayUtils.isEmpty(oldValueBypeArray)) {
                        oldValue = (T) SerializerAndDeserializer.unserialize(oldValueBypeArray);
                    }
                    Transaction transaction = jedis.multi();

                    T newValue = redisCallBack.computeNewValue(oldValue);

                    transaction.set(realKey, SerializerAndDeserializer.serialize(newValue));

                    if (transaction.exec() != null) {
                        jedis.unwatch();
                        break;
                    } else {
                        LOGGER.warn("Redis线程安全的设值失败，需要重试。 KEY值为：[" + key + "]");
                        jedis.unwatch();
                    }
                } catch (Exception ex) {
                    flag = true;
                    jedis.unwatch();
                    throw ex;
                }

            }
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis线程安全的设值失败，发生异常。", ex);
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }

        LOGGER.info("Redis线程安全的设值操作结束，KEY值为：[" + key + "]");
    }

    public void returnResource(ShardedJedisPool shardedJedisPool, ShardedJedis shardedJedis, boolean broken) {
        if (broken) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
        } else {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }

    public void returnResource(JedisPool jedisPool, Jedis jedis, boolean broken) {
        if (broken) {
            jedisPool.returnBrokenResource(jedis);
        } else {
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 批量获取缓存值（一次不要超过20个）
     *
     * @param key
     * @param value
     */
    public List<String> mget(String...key) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return null;
            }
            return jedis.mget(key);
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis mget数据发生异常 key为：" + key, ex);
            return null;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }

    }

    public Jedis getClient() {
        Jedis jedis = null;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return null;
            }
            return jedis;
        } catch (Exception ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
            return null;
        }
    }

    /**
     * 模糊查询Keys是否存在
     *
     * @param keyPattern
     * @return
     */
    public Set<String> keys(String keyPattern) {
        Jedis jedis = null;
        boolean flag = false;
        try {
            jedis = masterJedisPool.getResource();
            if (jedis == null) {
                return null;
            }
            return jedis.keys(keyPattern);
        } catch (Exception ex) {
            flag = true;
            LOGGER.error("Redis mget数据发生异常 key为：" + keyPattern, ex);
            return null;
        } finally {
            this.returnResource(masterJedisPool, jedis, flag);
        }
    }
}
