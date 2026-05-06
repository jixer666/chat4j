package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.platform.domain.dto.MessageReadCountDTO;
import com.abc.chat4j.platform.domain.dto.MessageReadDTO;
import com.abc.chat4j.platform.domain.entity.MessageRead;
import com.abc.chat4j.platform.mapper.MessageReadMapper;
import com.abc.chat4j.platform.service.MessageReadService;
import com.abc.chat4j.platform.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageReadServiceImpl extends ServiceImpl<MessageReadMapper, MessageRead> implements MessageReadService {

    @Autowired
    private MessageReadMapper messageReadMapper;

    @Override
    public List<Long> markMessageRead(MessageReadDTO messageReadDTO) {
        checkMarkMessageReadParams(messageReadDTO);
        // 筛选需要回执的消息
        List<Long> recepitMsgIdList = SpringUtil.getBean(MessageService.class).selectRecepitMessageByMsgIdList(messageReadDTO.getMsgIdList());
        if (CollectionUtil.isEmpty(recepitMsgIdList)) {
            return new ArrayList<>();
        }
        // 筛选已经保存的消息，防止重复保存
        List<Long> existMsgId = messageReadMapper.selectReadCountByUserIdAndMsgIdList(messageReadDTO.getUserId(), recepitMsgIdList);
        recepitMsgIdList.removeAll(existMsgId);
        if (CollectionUtil.isEmpty(recepitMsgIdList)) {
            return new ArrayList<>();
        }
        List<MessageRead> messageReadList = recepitMsgIdList.stream().map(item -> {
            MessageRead messageRead = new MessageRead();
            messageRead.setReadId(IdUtils.getId());
            messageRead.setMsgId(item);
            messageRead.setDevice(messageReadDTO.getDevice());
            messageRead.setUserId(messageReadDTO.getUserId());
            messageRead.setCreateTime(new Date());
            return messageRead;
        }).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(messageReadList)) {
            return new ArrayList<>();
        }
        messageReadMapper.insertMessageReadBatch(messageReadList);

        return recepitMsgIdList;
    }

    private void checkMarkMessageReadParams(MessageReadDTO messageReadDTO) {
        AssertUtils.isNotEmpty(messageReadDTO, "参数不能为空");
        AssertUtils.isNotEmpty(messageReadDTO.getMsgIdList(), "消息列表不能为空");
        AssertUtils.isNotEmpty(messageReadDTO.getUserId(), "用户ID不能为空");
        AssertUtils.isNotEmpty(messageReadDTO.getDevice(), "设备不能为空");
    }

    @Override
    public List<MessageReadCountDTO> selectReadCountByMsgIdList(List<Long> receiptMsgIdList) {
        if (CollectionUtil.isEmpty(receiptMsgIdList)) {
            return new ArrayList<>();
        }

        return messageReadMapper.selectReadCountByMsgIdList(receiptMsgIdList);
    }

    @Override
    public List<Long> selectReadUserIdListByMsgId(Long msgId) {
        AssertUtils.isNotEmpty(msgId, "消息ID不能为空");

        return messageReadMapper.selectReadUserIdListByMsgId(msgId);
    }

}
