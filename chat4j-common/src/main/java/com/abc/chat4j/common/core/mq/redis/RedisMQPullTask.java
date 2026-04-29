package com.abc.chat4j.common.core.mq.redis;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.annotation.RedisMQListener;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 基于Redis实现的MQ消息队列
 * @author LiJunXi
 * @date 2026/4/26
 */
@Slf4j
@Component
public class RedisMQPullTask implements CommandLineRunner {

    @Autowired(required = false)
    private List<RedisMQConsumer> consumers = new ArrayList<>();

    @Autowired
    @Qualifier("scheduledExecutorService")
    private ScheduledExecutorService executor;

    @Autowired
    private RedisMQTemplate redisMQTemplate;

    @Override
    public void run(String... args) {
        log.info("开启RedisMQ消息队列消费");
        for (RedisMQConsumer consumer : consumers) {
            // 注解参数
            RedisMQListener annotation = consumer.getClass().getAnnotation(RedisMQListener.class);
            String queue = annotation.queue();
            int batchSize = annotation.batchSize();
            int period = annotation.period();
            // 获取泛型类型
            Type superClass = consumer.getClass().getGenericSuperclass();
            Type type = ((ParameterizedType)superClass).getActualTypeArguments()[0];
            // 执行消费
            executor.execute(new Runnable() {
                List<Object> dataList = new ArrayList<>();
                @Override
                public void run() {
                    try {
                        if (consumer.isReady()) {
                            // 拉取一个批次的数据
                            dataList = pullBatch(ImQueueConstant.getQueueKey(queue), batchSize);
                            if (CollectionUtils.isNotEmpty(dataList)) {
                                consumer.onMessage(
                                        // 重要：转换对象
                                        dataList.stream().map(item -> {
                                            JSONObject jsonObject = (JSONObject) item;
                                            return jsonObject.toJavaObject(type);
                                        }).collect(Collectors.toList())
                                );
                            }
                        }
                    } catch (Exception e) {
                        log.error("数据消费异常,队列:{}", queue, e);
                        // 出现异常，10s后再重新尝试消费
                        executor.schedule(this, 10, TimeUnit.SECONDS);
                        return;
                    }
                    // 继续消费下一批次的数据
                    if (!executor.isShutdown()) {
                        if (dataList.size() < batchSize) {
                            // 数据已经消费完，等待下一个周期继续拉取
                            executor.schedule(this, period, TimeUnit.MILLISECONDS);
                        } else {
                            // 数据没有消费完，直接开启下一个消费周期
                            executor.execute(this);
                        }
                    }
                }
            });
        }
    }

    private List<Object> pullBatch(String key, Integer batchSize) {
        List<Object> objects = new LinkedList<>();
        if (redisMQTemplate.isSupportBatchPull()) {
            // 版本大于6.2，支持批量拉取
            objects = redisMQTemplate.opsForList().leftPop(key, batchSize);
        } else {
            // 版本小于6.2，只能逐条拉取
            Object obj = redisMQTemplate.opsForList().leftPop(key);
            while (!Objects.isNull(obj) && objects.size() < batchSize) {
                objects.add(obj);
                obj = redisMQTemplate.opsForList().leftPop(key);
            }
            if (!Objects.isNull(obj)){
                objects.add(obj);
            }
        }
        return objects;
    }
}
