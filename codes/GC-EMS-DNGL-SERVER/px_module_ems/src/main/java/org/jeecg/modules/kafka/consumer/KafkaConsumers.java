package org.jeecg.modules.kafka.consumer;

//import com.google.common.base.Splitter;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.jeecg.modules.tpmEquipment.entity.TpmEquipment;
//import org.jeecg.modules.tpmEquipment.service.ITpmEquipmentService;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.Objects;
//import java.util.Properties;
//import java.util.stream.Collectors;
//
// 这个代码暂时不用了，有更简洁的使用方法，就是TestConsumer的@KafkaListener(topicPattern = "thing___.*.___property")
//
///**
// * 因为topic是动态的，重构consumer
// */
//@Service
//@Slf4j
public class KafkaConsumers {
//public class KafkaConsumers implements InitializingBean {
//
//    @Autowired
//    private ITpmEquipmentService equipmentService;
//
//    /**
//     * 消费者
//     */
//    private static KafkaConsumer<String, String> consumer;
//    /**
//     * topic
//     */
//    private List<String> topicList;
//
//    public String getNewTopic() {
//        // 从数据库中获取设备信息
//        List<TpmEquipment> equipments = equipmentService.list();
////        String topicList = equipments.stream().map(equipment -> "/sys/" + equipment.getSpec() + "/" +
////                                                equipment.getEquipmentcode() + "/thing/event/property/post").
////                                    collect(Collectors.joining(","));
//        String topicList = equipments.stream().map(equipment -> equipment.getEquipmentcode()).
//                collect(Collectors.joining(","));
//        return topicList;
//    }
//
//    /**
//     * 初始化消费者（配置写死是为了快速测试，请大家使用配置文件）
//     *
//     * @param topicList
//     * @return
//     */
//    public KafkaConsumer<String, String> getInitConsumer(List<String> topicList) {
//        //配置信息
//        Properties props = new Properties();
//        //kafka服务器地址
//        props.put("bootstrap.servers", "127.0.0.1:9092");
//        //必须指定消费者组
//        props.put("group.id", "grout.test");
//        //设置数据key和value的序列化处理类
//        props.put("key.deserializer", StringDeserializer.class);
//        props.put("value.deserializer", StringDeserializer.class);
//        //创建消息者实例
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        //订阅topic的消息
//        consumer.subscribe(topicList);
//        return consumer;
//    }
//
//    /**
//     * 开启消费者线程
//     * 异常请自己根据需求自己处理
//     */
//    @Override
//    public void afterPropertiesSet() {
//        // 初始化topic
//        topicList = Splitter.on(",").splitToList(Objects.requireNonNull(getNewTopic()));
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(topicList)) {
//            consumer = getInitConsumer(topicList);
//            // 开启一个消费者线程
//            new Thread(() -> {
//                while (true) {
//                    // 模拟从配置源中获取最新的topic（字符串，逗号隔开）
//                    final List<String> newTopic = Splitter.on(",").splitToList(Objects.requireNonNull(getNewTopic()));
//                    // 如果topic发生变化
//                    if (!topicList.equals(newTopic)) {
//                        log.info("topic 发生变化：newTopic:{},oldTopic:{}-------------------------", newTopic, topicList);
//                        // method one：重新订阅topic:
//                        topicList = newTopic;
//                        consumer.subscribe(newTopic);
//                        // method two：关闭原来的消费者，重新初始化一个消费者
//                        //consumer.close();
//                        //topicList = newTopic;
//                        //consumer = getInitConsumer(newTopic);
//                        continue;
//                    }
//                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
//                    for (ConsumerRecord<String, String> record : records) {
//                        System.out.println("key:" + record.key() + "" + ",value:" + record.value());
//                    }
//                }
//            }).start();
//        }
//    }
}

