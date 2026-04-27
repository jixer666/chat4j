package com.abc.chat4j.im.netty.process;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.exception.GlobalException;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.netty.NettyUtil;
import com.abc.chat4j.im.netty.UserChannelCtxMap;
import com.abc.chat4j.im.netty.process.model.ErrorMessage;
import com.abc.chat4j.im.netty.process.model.ImSendContext;
import com.abc.chat4j.system.domain.vo.UserVO;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
public abstract class MessageProcess<T> {

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    public MessageProcess() {
        // 获取泛型类型
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superClass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            this.clazz = (Class<T>) actualTypeArguments[0];
        }
    }

    public void doProcess(T data) {

    }

    public void doProcess(ChannelHandlerContext ctx, T data) {

    }

    public void process(ImSendContext imSendContext) {
        try {
            T data = BeanUtil.toBean(imSendContext.getData(), clazz);
            if (Objects.isNull(imSendContext.getCtx())) {
                doProcess(data);
            } else {
                doProcess(imSendContext.getCtx(), data);
            }
        } catch (GlobalException e) {
            if (Objects.nonNull(imSendContext.getCtx())) {
                sendErrorMessage(imSendContext.getCtx(), e.getCode(), e.getMessage());
            }
        }
    }

    private void sendErrorMessage(ChannelHandlerContext ctx, Integer code, String msg) {
        ImSendInfo sendInfo = new ImSendInfo();
        sendInfo.setType(ImMessageTypeEnum.ERROR.getType());
        sendInfo.setData(new ErrorMessage(code, msg));
        sendMessage(ctx, sendInfo);
    }

    private void sendMessage(ChannelHandlerContext ctx, ImSendInfo imSendInfo) {
        ctx.channel().writeAndFlush(imSendInfo);
    }

}
