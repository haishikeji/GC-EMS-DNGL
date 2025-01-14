package org.jeecg.modules.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

@Component
@Slf4j
public class TestProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    //自定义topic
//    public static final String TOPIC_TEST = "visible";
//    public static final String GROUP_TEST = "grout.test";
//
    public void send(String topicname, Object obj) {
        String obj2String = JSON.toJSONString(obj);
        log.info("准备发送消息为：{}", obj2String);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicname, obj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(@NotNull Throwable throwable) {
                //发送失败的处理
                log.info(topicname + " - 生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                log.info(topicname + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });
    }
}
