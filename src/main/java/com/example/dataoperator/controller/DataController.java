package com.example.dataoperator.controller;

import com.alibaba.fastjson.JSON;
import com.example.dataoperator.dto.R;
import com.example.dataoperator.dto.Student;
import com.example.dataoperator.dto.StudentVo;
import com.example.dataoperator.reposity.JedisService;
import com.example.dataoperator.reposity.RedisReposity;
import com.example.dataoperator.service.StudentService;
import com.example.dataoperator.sub.StudentSub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("data")
public class DataController {

    // 分布式锁20秒过期
    private final int DISTRIBUTED_LOCK_EXPIRE_TIME = 20 * 1000;

    //休眠毫秒数
    private final long SLEEP_TIME = 200;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StudentService studentService;

    @Autowired
    RedisReposity redisReposity;

    @Autowired
    JedisService jedisService;


    @RequestMapping("save")
    public R save(StudentVo vo){
        if(vo.getType().equals("mysql")){
            studentService.save(vo);
        }else{
            //redisReposity.save(vo);
            //saveByJedis(vo);
            pubSub();
        }
        return R.ok();
    }


    public void saveByJedis(StudentVo vo){
        try {
            jedisService.hset("studentJ",vo.getName(), JSON.toJSONString(vo));
        } catch (Exception e) {
            logger.info("jedis异常");
        }
    }


    public void pubSub(){
        SubThread subThread = new SubThread();
        subThread.start();
        PubThread pubThread = new PubThread();
        pubThread.start();

    }

    class SubThread extends Thread {

        @Override
        public void run() {
            jedisService.subscribe(new StudentSub(), "msg");
        }
    }

    class PubThread extends Thread {

        @Override
        public void run() {
            jedisService.publish("msg", "hello student");
        }
    }


    public void getLock(){
        String requestId = UUID.randomUUID().toString();

        //分布式锁,锁的名字
        String distributeLockKeyOfBatchAllocAgent = "distrlock";

        try{
            // 3. 获取分布式锁
            while (true) {
                boolean isLock = jedisService.tryGetDistributedLock(distributeLockKeyOfBatchAllocAgent, requestId, DISTRIBUTED_LOCK_EXPIRE_TIME);

                if (!isLock) {
                    logger.info("获取锁失败, 休眠{}ms. key:{}", SLEEP_TIME, distributeLockKeyOfBatchAllocAgent);

                    Thread.sleep(SLEEP_TIME);
                    continue;
                }
                break;
            }

            //TODO 拿到锁，继续进行下一步操作


        }catch (Exception e){

        }finally {

            try {
                //释放分布式锁
                jedisService.releaseDistributedLock(distributeLockKeyOfBatchAllocAgent, requestId);
            }catch (Exception e){

            }

        }


    }

}
