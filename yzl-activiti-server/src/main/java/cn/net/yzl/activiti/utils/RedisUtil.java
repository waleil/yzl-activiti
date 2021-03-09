package cn.net.yzl.activiti.utils;

import cn.net.yzl.logger.annotate.SysAccessLog;
import cn.net.yzl.logger.enums.DefaultDataEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法                    不含通用方法
 * 针对所有的List 都是以l开头的方法
 */
@Component
@Slf4j
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    //=============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.UPDATE)
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.EXISTS)
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.DEL)
    public void del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return;
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public String getStr(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        try {
            Object result = redisTemplate.opsForValue().get(key);
            if (Objects.nonNull(result)) {
                return String.valueOf(result);
            }
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.ADD
    )
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.ADD)
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.DEL)
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        try {
            return redisTemplate.opsForValue().increment(key, -delta);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.QUERY)
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.QUERY)
    public Map<Object, Object> hmget(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.ADD)
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.ADD)
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    @SysAccessLog(logKeyParamName = {"key","item"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.ADD)
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    @SysAccessLog(logKeyParamName = {"key","item"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.ADD)
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.DEL)
    public void hdel(String key, Object... item) {
        try {
            redisTemplate.opsForHash().delete(key, item);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return;
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS
            ,action = DefaultDataEnums.Action.QUERY)
    public boolean hHasKey(String key, String item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public double hincr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, by);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.DEL)
    public double hdecr(String key, String item, double by) {
        try {
            return redisTemplate.opsForHash().increment(key, item, -by);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.DEL)
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.QUERY)
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.ADD)
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.UPDATE)
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    @SysAccessLog(logKeyParamName = {"key"},
            source = DefaultDataEnums.Source.REDIS,action = DefaultDataEnums.Action.DEL)
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("redis调用失败,code:{}", e);
//            e.printStackTrace();
            return 0;
        }
    }
}