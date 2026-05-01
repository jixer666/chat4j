package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.context.ConversationQueryContext;
import com.abc.chat4j.platform.domain.dto.ConversationPullDTO;
import com.abc.chat4j.platform.domain.entity.Conversation;
import com.abc.chat4j.platform.domain.vo.ConversationVO;
import com.abc.chat4j.platform.domain.vo.RoomInfoVO;
import com.abc.chat4j.platform.mapper.ConversationMapper;
import com.abc.chat4j.platform.service.ConversationService;
import com.abc.chat4j.platform.service.RoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

    @Autowired
    private ConversationMapper conversationMapper;

    @Autowired
    private RoomService roomService;

    @Override
    public List<ConversationVO> selectConversationList(ConversationPullDTO conversationPullDTO) {
        checkConversationPullDTOParams(conversationPullDTO);

        ConversationQueryContext context = new ConversationQueryContext();
        context.setUserId(SecurityUtils.getUserId());
        context.setMinUpdateTime(context.getMinUpdateTime());
        List<Conversation> conversationList = selectConversationList(context);

        return buildConversationVOList(conversationList);
    }

    private void checkConversationPullDTOParams(ConversationPullDTO conversationPullDTO) {
        AssertUtils.isNotEmpty(conversationPullDTO, "参数不能为空");
        // 会话最大取15天内的
        Date minUpdateTime = conversationPullDTO.getMinUpdateTime();
        Date maxMinUpdateTime = DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_CONVERSATION_DAYS));
        conversationPullDTO.setMinUpdateTime(Objects.isNull(minUpdateTime) ? maxMinUpdateTime :
                minUpdateTime.before(maxMinUpdateTime) ? minUpdateTime : maxMinUpdateTime);
    }

    private List<ConversationVO> buildConversationVOList(List<Conversation> conversationList) {
        List<Long> roomIdList = conversationList.stream().map(Conversation::getRoomId).collect(Collectors.toList());
        List<RoomInfoVO> roomInfoList = roomService.selectRoomInfoVOListByRoomIds(roomIdList);
        Map<Long, RoomInfoVO> roomInfoMap = roomInfoList.stream().collect(Collectors.toMap(RoomInfoVO::getRoomId, Function.identity()));
        return conversationList.stream().map(item -> {
            ConversationVO conversationVO = BeanUtil.copyProperties(item, ConversationVO.class);
            RoomInfoVO roomInfo = roomInfoMap.get(item.getRoomId());
            conversationVO.setRoomInfo(roomInfo);
            return conversationVO;
        }).collect(Collectors.toList());
    }

    private List<Conversation> selectConversationList(ConversationQueryContext context) {
        return conversationMapper.selectConversationList(context);
    }

    @Override
    public void updateActiveTimeByConversationId(Long conversationId, Long userId, Date date) {
        AssertUtils.isNotEmpty(conversationId, "会话不能为空");
        AssertUtils.isNotEmpty(date, "更新日期不能为空");
        conversationMapper.updateActiveTimeByConversationId(conversationId, userId, date);
    }
}
