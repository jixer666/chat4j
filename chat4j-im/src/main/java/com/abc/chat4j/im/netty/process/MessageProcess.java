package com.abc.chat4j.im.netty.process;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abc.chat4j.common.exception.GlobalException;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.im.client.ImClient;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.netty.process.model.ErrorMessage;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
public abstract class MessageProcess<T> {

    private Class<T> clazz;

    private ImClient imClient;

    @SuppressWarnings("unchecked")
    public MessageProcess() {
        // 获取泛型类型
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            this.clazz = (Class<T>) actualTypeArguments[0];
        }
        imClient = SpringUtil.getBean(ImClient.class);
    }

    public void doProcess(ChannelHandlerContext ctx, T data) {

    }

    public void checkMessageContentDetails(T data) {

    }

    public void checkMessageContent(Object data) {
        // 通用检查
        AssertUtils.isNotEmpty(data, "消息内容不能为空");
        T message = BeanUtil.toBean(data, clazz);
        // 检查各个策略的字段
        checkMessageContentDetails(message);
    }

    public void process(ImSendContext<?> imSendContext) {
        try {
            if (Objects.isNull(imSendContext.getCtx())) {
                sendMessage(imSendContext);
            } else {
                T data = BeanUtil.toBean(imSendContext.getMsgData(), clazz);
                doProcess(imSendContext.getCtx(), data);
            }
        } catch (GlobalException e) {
            if (Objects.nonNull(imSendContext.getCtx())) {
                // 推送IM错误提示信息
                sendErrorMessage(imSendContext.getCtx(), e.getCode(), e.getMessage());
            } else {
                // 普通请求的错误提示信息
                throw new GlobalException(e.getMessage());
            }
        }
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, Integer code, String msg) {
        ImSendInfo sendInfo = new ImSendInfo();
        sendInfo.setType(ImMessageTypeEnum.ERROR.getType());
        sendInfo.setContent(new ErrorMessage(code, msg));
        sendMessage(ctx, sendInfo);
    }

    private void sendMessage(ChannelHandlerContext ctx, ImSendInfo imSendInfo) {
        ctx.channel().writeAndFlush(imSendInfo);
    }

    private void sendMessage(ImSendContext<?> context) {
        imClient.sendMessage(context);
    }
}
