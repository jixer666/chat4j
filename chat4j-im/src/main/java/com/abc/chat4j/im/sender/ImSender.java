package com.abc.chat4j.im.sender;

import com.abc.chat4j.common.constant.CacheConstants;
import com.abc.chat4j.common.core.mq.redis.RedisMQTemplate;
import com.abc.chat4j.common.constant.ImQueueConstant;
import com.abc.chat4j.common.domain.enums.DeviceEnum;
import com.abc.chat4j.common.util.RedisUtils;
import com.abc.chat4j.im.netty.process.model.ImReceiveContext;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import com.abc.chat4j.im.netty.process.model.ImSendUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/4/28
 */
@Component
public class ImSender {

    @Autowired
    private RedisMQTemplate redisMQTemplate;

    public <T> void sendMessage(ImSendContext<T> context) {
        List<Long> targetUserIdList = context.getTargetUserIdList();
        targetUserIdList.add(context.getImSendUserInfo().getUserId());
        for (Long userId : targetUserIdList) {
            for (DeviceEnum device : DeviceEnum.values()) {
                if (userId.equals(context.getImSendUserInfo().getUserId()) && device.getType().equals(context.getImSendUserInfo().getDevice())) {
                    // 用户相同的设备不进行推送
                    continue;
                }
                // 获取用户对应在线的服务器
                String serverKey = CacheConstants.getFinalKey(CacheConstants.USER_LOGIN_SERVER, userId, device.getType());
                String serverId = RedisUtils.get(serverKey);
                if (Objects.isNull(serverId)) {
                    // 用户离线状态
                    continue;
                }
                // 在线用户进行推送到对应的服务器
                String queueKey = ImQueueConstant.getQueueKey(context.getQueue(), serverId);
                // 构造接收上下文
                ImReceiveContext receiveContext =  new ImReceiveContext();
                receiveContext.setData(context.getData());
                receiveContext.setImReceiveUserInfo(new ImSendUserInfo(userId, device.getType()));
                redisMQTemplate.opsForList().rightPush(queueKey, receiveContext);
            }
        }
    }

    public boolean isOnline(Long userId, Integer device) {
        return RedisUtils.hasKey(
                CacheConstants.getFinalKey(CacheConstants.getFinalKey(CacheConstants.USER_LOGIN_SERVER, userId, device))
        );
    }
}
