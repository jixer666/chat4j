package com.abc.chat4j.im.netty.process;

import cn.hutool.core.bean.BeanUtil;
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
        T data = BeanUtil.toBean(imSendContext.getData(), clazz);
        if (Objects.isNull(imSendContext.getCtx())) {
            doProcess(data);
        } else {
            doProcess(imSendContext.getCtx(), data);
        }
    }

}
