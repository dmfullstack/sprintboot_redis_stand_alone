package com.h2h.redis_stand_alone.kit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisKit {

//    @Autowired
    protected RedisTemplate redisTemplate;

    /**
     * 格式化 redis 中 key的存储，以免出现乱码
     * @param redisTemplate
     */
//    @Autowired(required = false)
//    public void setRedisTemplate(RedisTemplate redisTemplate) {
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        this.redisTemplate = redisTemplate;
//    }

    /**
     * 格式化 redis 中 key与value 的存储，以免出现乱码
     * @decription 连接docker中redis时value也会出现乱码 使用String格式时 value存储 只能为String格式
     * @param redisTemplate
     */
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        this.redisTemplate = redisTemplate;
    }



    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param expire  过期时间(单位:秒),传入 -1 时表示不设置过期时间
     * @return        缓存的对象
     */
    public boolean setCacheObject(String key, Object value, long expire) {
        boolean result= false;
        redisTemplate.opsForValue().set(key,value);
        if (expire != -1) {
            result = redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return result;


    }



    /**
     * 缓存Set
     * @param key     缓存的键值
     * @param value   缓存的值
     * @return        受影响行数
     */
    public  boolean addToSet(String key, String value) {
        boolean flag =false;
        SetOperations setOperations =redisTemplate.opsForSet();
        int result=  setOperations.add(key,value).intValue();
        if(result>0){
            flag=true;
        }
        return flag;
    }


    /**
     * 移除Set集合中的成员
     * @param key
     * @param value
     */
    public boolean Srem(String key,String value){
        boolean flag =false;
        SetOperations setOperations =redisTemplate.opsForSet();
        long result=  setOperations.remove(key,value);
        if(result>0){
            flag=true;
        }
        return flag;

    }


    /**
     * 获取set的值
     * @param key     缓存的键值
     * @return        受影响行数
     */
    public Set getSet(String key) {
        SetOperations setOperations =redisTemplate.opsForSet();
        Set  set=  setOperations.members(key);
        return set;
    }

    /**
     * @Name: 获取set集合元素个数
     * @Author: 程文远（作者）
     * @Version: V1.00 （版本号）
     * @Create Date: 2018年1月29日下午4:05:35
     * @param key
     * 			set集合key
     * @Return: 返回个数
     */
    public long getSetObjNum(String key){
        SetOperations setOperations =redisTemplate.opsForSet();
        return setOperations.size(key);
    }

    /**
     * 删除一个键值
     * @param key     缓存的键
     * @return    void
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 获得缓存的基本对象。
     * @param key        缓存键值
     * @return            缓存键值对应的数据
     */
    public Object getCacheObject(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        return new ResponseEntity(o, HttpStatus.OK);
    }

    /**
     * 缓存List数据
     * @param key        缓存的键值
     * @param dataList    待缓存的List数据
     * @return            缓存的对象
     */
    public Object setCacheList(String key, List<Object> dataList)
    {
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        if(null != dataList)
        {
            int size = dataList.size();
            for(int i = 0; i < size ; i ++)
            {
                listOperation.rightPush(key,dataList.get(i));
            }
        }
        return listOperation;
    }

    /**
     * 获得缓存的list对象
     * @param key    缓存的键值
     * @return        缓存键值对应的数据
     */
    public List<Object> getCacheList(String key)
    {
        List<Object> dataList = new ArrayList<Object>();
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for(int i = 0 ; i < size ; i ++)
        {
            dataList.add(listOperation.leftPop(key));
        }
        return dataList;
    }

    /**
     * 获得缓存的list对象
     * @param key    缓存的键值
     * @return        缓存键值对应的数据
     */
    public List<Object> getCacheList(String key,int start)
    {
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);
        return listOperation.range(key, start, size);
    }

    /**
     * 获得缓存的list对象
     * @Title: range
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param @param key
     * @param @param start
     * @param @param end
     * @param @return
     * @return List<T>    返回类型
     * @throws
     */
    public List<Object> range(String key, long start, long end)
    {
        ListOperations<String, Object> listOperation = redisTemplate.opsForList();
        return listOperation.range(key, start, end);
    }

    /**
     * list集合长度
     * @param key
     * @return
     */
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     *
     * @param key
     * @param index 位置
     * @param obj
     *            value 值
     * @return 状态码
     * */
    public void listSet(String key, int index, Object obj) {
        redisTemplate.opsForList().set(key, index, obj);
    }

    /**
     * 向List尾部追加记录
     *
     * @param key
     * @param obj
     * @return 记录总数
     * */
    public long leftPush(String key, Object obj) {
        return redisTemplate.opsForList().leftPush(key, obj);
    }

    /**
     * 向List头部追加记录
     *
     * @param key
     * @param obj
     * @return 记录总数
     * */
    public long rightPush(String key, Object obj) {
        return redisTemplate.opsForList().rightPush(key, obj);
    }

    /**
     * 算是删除吧，只保留start与end之间的记录
     *
     * @param key
     * @param start 记录的开始位置(0表示第一条记录)
     * @param end 记录的结束位置（如果为-1则表示最后一个，-2，-3以此类推）
     * @return 执行状态码
     * */
    public void trim(String key, int start, int end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 删除List中c条记录，被删除的记录值为value
     *
     * @param key
     * @param i 要删除的数量，如果为负数则从List的尾部检查并删除符合的记录
     * @param obj 要匹配的值
     * @return 删除后的List中的记录数
     * */
    public long remove(String key, long i, Object obj) {
        return redisTemplate.opsForList().remove(key, i, obj);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     * @param key     缓存的键值
     * @param value   缓存的值
     * @param expire  过期时间 指定日期
     * @return        缓存的对象
     */
    public boolean setCacheObjectAt(String key, Object value, Date expire) {
        boolean result= false;
        redisTemplate.opsForValue().set(key,value);
        if (expire.getTime()>new Date().getTime()) {
            result = redisTemplate.expireAt(key, expire);
        }
        return result;


    }

    /**
     * 获取key剩余时间
     * @param key
     * @return
     */
    public long getTime(String key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 位图存储
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public boolean setbit(String key,long offset,boolean value){
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.setBit(key, offset, value);
    }

    /**
     * 位图存储
     * @param key
     * @param offset
     * @return
     */
    public boolean getbit(String key,long offset){
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.getBit(key, offset);
    }

    /**
     * 统计bit位为1的总数
     * @param key
     */
    public long bitCount(final String key) {
        return (long)redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                result = connection.bitCount(key.getBytes());
                return result;
            }
        });
    }

    public void changeDatabase(int i){
        JedisConnectionFactory connectionFactory = (JedisConnectionFactory)redisTemplate.getConnectionFactory();
        connectionFactory.setDatabase(2);
    }
}
