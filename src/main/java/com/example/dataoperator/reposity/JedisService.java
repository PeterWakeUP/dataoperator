package cn.tdw.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 封装redis 缓存服务器服务接口
 * @author Star
 *
 * 2018-04-11 上午10:09:18
 */
@Service
public class JedisService {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 通过key删除（字节）
     * @param key
     */
    public void del(byte [] key)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        jedis.del(key);
        conn.close();
    }
    /**
     * 通过key删除
     * @param key
     */
    public void del(String key)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        jedis.del(key);
        conn.close();
    }

    /**
     * 添加key value 并且设置存活时间
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key,String value,int liveTime)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        jedis.set(key, value);
        jedis.expire(key, liveTime);
        conn.close();
    }
    /**
     * 添加key value
     * @param key
     * @param value
     */
    public void set(String key,String value)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        jedis.set(key, value);
        conn.close();
    }
    /**添加key value (字节)(序列化)
     * @param key
     * @param value
     */
    public void set(byte [] key,byte [] value)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        jedis.set(key, value);
        conn.close();
    }
    /**
     * 获取redis value (String)
     * @param key
     * @return
     */
    public String get(String key)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        String value = jedis.get(key);
        conn.close();
        return value;
    }
    /**
     * 获取redis value (byte [] )(反序列化)
     * @param key
     * @return
     */
    public byte[] get(byte [] key)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        byte[] reslut = jedis.get(key);
        conn.close();
        return reslut;
    }

    /**
     * 通过正则匹配keys
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Set<String> reslut = jedis.keys(pattern);
        conn.close();
        return reslut;
    }

    /**
     * 检查key是否已经存在
     * @param key
     * @return
     */
    public boolean exists(String key)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        boolean result = jedis.exists(key);
        conn.close();
        return result;
    }
    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime)throws Exception {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        conn.close();
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean releaseDistributedLock(String lockKey, String requestId)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        conn.close();
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    public String hget(String key,String field)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        String result = jedis.hget(key,field);
        conn.close();
        return result;
    }
    public Map<String,String> hgetAll(String key)throws Exception {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Map<String,String> result = jedis.hgetAll(key);
        conn.close();
        return result;
    }

    public long hdel(String key,String userid)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        long result = jedis.hdel(key,userid);
        conn.close();
        return result;
    }

    public long hset(String key,String field,String value)throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        long result = jedis.hset(key,field,value);
        conn.close();
        return result;
    }

    public long hincrby(String key, String field, int value){
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        long result = jedis.hincrBy(key,field,value);
        conn.close();
        return result;
    }

    public List<String> hmget(String key, String[] arryField) {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        List<String> result = jedis.hmget(key, arryField);
        conn.close();
        return result;
    }

    public Long delete(String key) throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Long result = jedis.del(key);
        conn.close();
        return result;
    }

    public Long incr(String key) throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Long result = jedis.incr(key);
        conn.close();
        return result;
    }

    public Long incrBy(String key,int value) throws Exception{
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Long result = jedis.incrBy(key,value);
        conn.close();
        return result;
    }

    public Long ttl(String key)throws Exception {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        Long result = jedis.ttl(key);
        conn.close();
        return result;
    }

    private JedisService(){

    }
    //操作redis客户端
    private static Jedis jedis;
    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    /**
     * 按降序获取集合元素，包含分值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Tuple> getSortedSetByRangeDescWithScores(String key, int start, int end){

        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        Set<Tuple> set  = jedis.zrevrangeWithScores(key, start, end);
        conn.close();
        return set;
    }

    /**
     * 按升序获取集合元素，包含分值
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Tuple> getSortedByRangeSetAscWithScores(String key, int start, int end){

        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        Set<Tuple> set  = jedis.zrangeWithScores(key, start, end);
        conn.close();
        return set;
    }

    /**
     * 按升序获取集合元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> getSortedSetByRangeDesc(String key, int start, int end){

        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        Set<String> set = jedis.zrevrange(key, start, end);
        conn.close();
        return set;
    }

    /**
     * 按升序获取集合元素，包含分值
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zrange(String key, long min, long max) {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Set<String> rtn = jedis.zrange(key, min, max);
        conn.close();
        return rtn;
    }

    /**
     * 按升序获取集合元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> getSortedSetByRangeAsc(String key, int start, int end){

        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        Set<String> set =jedis.zrange(key, start, end);
        conn.close();
        return set;
    }

    /**
     * 按降序获取集合元素，包含分值
     * @param key
     * @param count     总数
     * @return
     */
    public Set<Tuple> getSortedSetByRangeDescWithScoresFromTop(String key, int count){
        if(count <= 1){
            return Collections.emptySet();
        }

        return getSortedSetByRangeDescWithScores(key, 0, count - 1);
    }

    /**
     * 按升序获取集合元素，包含分值
     * @param key
     * @param count     总数
     * @return
     */
    public Set<Tuple> getSortedSetByRangeAscWithScoresFromTop(String key, int count){
        if(count <= 1){
            return Collections.emptySet();
        }

        return getSortedByRangeSetAscWithScores(key, 0, count - 1);
    }

    /**
     * 获取当前成员的排名--升序
     * @param   key
     * @param   member
     * @return  排名
     */
    public long getMemberRankAsc(String key, String member){

        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Long value = jedis.zrank(key, member);
        conn.close();
        return value;
    }

    /**
     * 获取当前成员的排名--降序
     * @param   key
     * @param   member
     * @return  排名
     */
    public Long getMemberRankDesc(String key, String member){

        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Long value = jedis.zrevrank(key, member);
        conn.close();
        return value;
    }

    /**
     * 获取当前成员的分值
     * @param   key
     * @param   member
     * @return  排名
     */
    public Double getMemberScore(String key, String member){

        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        Double rtn =  jedis.zscore(key, member);
        conn.close();
        return rtn;
    }

    /**
     * 有序集合中对指定成员的分数加上增量 increment
     * @param key
     * @param member
     * @param increment
     */
    public Double zincrBy(String key, String member, double increment) {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Double rtn = jedis.zincrby(key, increment, member);
        conn.close();
        return rtn;
    }

    /**
     * 有序集合中对指定成员的分数加上增量 increment
     * @param key
     * @param member
     * @param increment
     * return 1: 增加新成员表, 0:原成员值更新
     */
    public Long zadd(String key, String member, double increment) {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Long rtn = jedis.zadd(key, increment, member);
        conn.close();
        return rtn;
    }

    /**
     * 按照排序,删除指定排名的成员（排名为升序）
     * eg. 升序保留前十名   start = 10， end=-1   (删除11到最后一名)
     *     降序保留前十名   start = 0， end=-11   (删除第一名到倒数11名，保留倒数第十到倒数第一名，即降序的第一到第十名)
     * @param key
     * @param start
     * @param end
     */
    public void zremrangeByRank(String key, int start, int end){
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        jedis.zremrangeByRank(key, start, end);
        conn.close();
    }

    /**
     * 获取集合成员数
     *
     * @param key
     */
    public Long zcard(String key) {
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();
        Long rtn = jedis.zcard(key);
        conn.close();
        return rtn;
    }

    /**
     * 设置过期时间
     * @param key
     * @param seconds
     */
    public void setExpireTime(String key, int seconds){
        RedisConnection conn = jedisConnectionFactory.getConnection();
        Jedis jedis = (Jedis) conn.getNativeConnection();

        jedis.expire(key, seconds);
        conn.close();
    }
}