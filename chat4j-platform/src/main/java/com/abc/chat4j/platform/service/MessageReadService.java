package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.dto.MessageReadCountDTO;
import com.abc.chat4j.platform.domain.dto.MessageReadDTO;
import com.abc.chat4j.platform.domain.entity.MessageRead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MessageReadService extends IService<MessageRead> {

    List<Long> markMessageRead(MessageReadDTO messageReadDTO);

    List<MessageReadCountDTO> selectReadCountByMsgIdList(List<Long> recepitMsgIdList);

    List<Long> selectReadUserIdListByMsgId(Long msgId);

}
