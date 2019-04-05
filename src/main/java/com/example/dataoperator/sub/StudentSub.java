package com.example.dataoperator.sub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

public class StudentSub extends JedisPubSub {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public StudentSub() {
    }

    @Override
    public void onMessage(String channel, String message) {       //收到消息会调用
        logger.info(String.format("channel:%s,message:%s", channel, message));
    }
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {    //订阅了频道会调用
        logger.info(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {   //取消订阅 会调用
        logger.info(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}
