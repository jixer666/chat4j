package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.dto.MessagePullDTO;
import com.abc.chat4j.platform.domain.entity.Message;
import com.abc.chat4j.platform.domain.vo.MessageVo;
import com.abc.chat4j.platform.mapper.MessageMapper;
import com.abc.chat4j.platform.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<MessageVo> loadOfflineMessage(MessagePullDTO messagePullDTO) {
        checkMessagePullDTO(messagePullDTO);

        Long userId = SecurityUtils.getUserId();
        Date lastMonthDate = DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_MESSAGE_DAYS));
        List<Message> messageList = messageMapper.selectOfflineList(messagePullDTO.getMinMsgId(), userId, lastMonthDate);


        return Collections.emptyList();
    }

    private void checkMessagePullDTO(MessagePullDTO messagePullDTO) {
        AssertUtils.isNotEmpty(messagePullDTO, "参数不能为空");
        if (Objects.isNull(messagePullDTO.getMinMsgId())) {
            messagePullDTO.setMinMsgId(0L);
        }
    }

}
