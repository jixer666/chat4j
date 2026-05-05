package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.context.MessageQueryContext;
import com.abc.chat4j.platform.domain.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectMessageList(MessageQueryContext messageQueryContext);

    void updateStatusByMsgIdList(@Param("status") Integer status,
                                 @Param("msgIdList") List<Long> msgIdList);

    List<Long> selectReceiptMessageByMsgIdList(@Param("isReceipt") Integer isReceipt,
                                               @Param("msgIdList") List<Long> msgIdList);
}
