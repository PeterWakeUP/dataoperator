package com.example.dataoperator.reposity;


import com.alibaba.fastjson.JSON;
import com.example.dataoperator.dto.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisReposity {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public void save(StudentVo vo){
        /*redisTemplate.opsForHash().put("student",vo.getName(), vo.getScore());
        System.out.println(redisTemplate.opsForHash().get("student",vo.getName()));*/

        /*stringRedisTemplate.opsForHash().put("student",vo.getName(), vo.getScore());
        System.out.println(stringRedisTemplate.opsForHash().get("student",vo.getName()));*/

        /*redisTemplate.opsForValue().set("student", JSON.toJSONString(vo));
        System.out.println(redisTemplate.opsForValue().get("student"));*/

        stringRedisTemplate.opsForValue().set("student", JSON.toJSONString(vo));
        System.out.println(stringRedisTemplate.opsForValue().get("student"));
    }


}
