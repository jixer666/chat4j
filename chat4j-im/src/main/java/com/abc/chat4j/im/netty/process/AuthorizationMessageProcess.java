package com.abc.chat4j.im.netty.process;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.constant.HttpStatus;
import com.abc.chat4j.common.domain.dto.LoginUserDTO;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.im.annotation.MessageType;
import com.abc.chat4j.im.domain.dto.ImSendInfo;
import com.abc.chat4j.im.domain.enums.ImMessageTypeEnum;
import com.abc.chat4j.im.netty.NettyUtil;
import com.abc.chat4j.im.netty.UserChannelCtxMap;
import com.abc.chat4j.im.netty.process.model.AuthorizationMessage;
import com.abc.chat4j.system.domain.vo.UserVO;
import com.abc.chat4j.system.service.TokenService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author LiJunXi
 * @date 2026/4/25
 */
@Component
@MessageType(type = ImMessageTypeEnum.AUTHORIZATION)
public class AuthorizationMessageProcess extends MessageProcess<AuthorizationMessage> {

    @Autowired
    private TokenService tokenService;

    @Override
    public void doProcess(ChannelHandlerContext ctx, AuthorizationMessage message) {
        checkAuthorizationMessageParams(message);

        NettyUtil.setAttr(ctx.channel(), NettyUtil.DEVICE, message.getDevice());
        LoginUserDTO loginUserDTO = tokenService.getLoginUserDTO(message.getToken());
        AssertUtils.isNotEmpty(loginUserDTO, HttpStatus.FORBIDDEN, "身份认证失败，请重新登录");
        NettyUtil.setAttr(ctx.channel(), NettyUtil.LOGIN_USER, loginUserDTO);

        UserChannelCtxMap.addChannelCtx(loginUserDTO.getUserId(), message.getDevice(), ctx);

        ImSendInfo sendInfo = new ImSendInfo();
        sendInfo.setType(ImMessageTypeEnum.AUTHORIZATION.getType());
        sendInfo.setData(BeanUtil.copyProperties(loginUserDTO.getUser(), UserVO.class));
        ctx.channel().writeAndFlush(sendInfo);
    }

    private void checkAuthorizationMessageParams(AuthorizationMessage message) {
        AssertUtils.isNotEmpty(message, "消息参数不能为空");
        AssertUtils.isNotEmpty(message.getToken(), HttpStatus.UNAUTHORIZED,"token不能为空");
        AssertUtils.isNotEmpty(message.getDevice(), "device不能为空");
    }

}
