package com.abc.chat4j.platform.mapper;

import com.abc.chat4j.platform.domain.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/4/26
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectOfflineList(@Param("msgId") Long minMsgId,
                                    @Param("userId") Long userId,
                                    @Param("date") Date lastMonthDate);
}
