package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.dto.MessageReadCountDTO;
import com.abc.chat4j.platform.domain.entity.MessageRead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageReadMapper extends BaseMapper<MessageRead> {

    void insertMessageReadBatch(@Param("messageReadList") List<MessageRead> messageReadList);

    List<MessageReadCountDTO> selectReadCountByMsgIdList(@Param("receiptMsgIdList") List<Long> receiptMsgIdList);

    List<Long> selectReadCountByUserIdAndMsgIdList(@Param("userId") Long userId,
                                                   @Param("msgIdList") List<Long> recepitMsgIdList);

    List<Long> selectReadUserIdListByMsgId(@Param("msgId") Long msgId);
}
