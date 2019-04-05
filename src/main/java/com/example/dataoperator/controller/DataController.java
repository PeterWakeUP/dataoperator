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

@RestController
@RequestMapping("data")
public class DataController {

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

}
