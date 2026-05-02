package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.context.ConversationCreateContext;
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

import java.util.*;
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
                minUpdateTime.before(maxMinUpdateTime) ? maxMinUpdateTime : minUpdateTime);
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

    @Override
    public List<Conversation> createConversation(ConversationCreateContext conversationContext) {
        checkCreateConversationParams(conversationContext);

        List<Conversation> conversationList = selectConversationBydUidListAndRoomId(conversationContext.getUserIdList(), conversationContext.getRoomId());
        Map<String, Conversation> conversationMap = conversationList.stream().collect(Collectors.toMap(conversation -> conversation.getUserId() + "_" + conversation.getRoomId(), Function.identity()));

        List<Conversation> insertConversationList = new ArrayList<>();
        List<Conversation> existConversationList = new ArrayList<>();
        for (Long useId : conversationContext.getUserIdList()) {
            // 是否已经存在了会话，存在就不再保存了
            String key = useId + "_" + conversationContext.getRoomId();
            boolean isContain = conversationMap.containsKey(key);
            if (isContain) {
                existConversationList.add(conversationMap.get(key));
                continue;
            }

            Conversation conversation = new  Conversation();
            conversation.setConversationId(IdUtils.getId());
            conversation.setRoomId(conversationContext.getRoomId());
            conversation.setUserId(useId);
            conversation.setIsMuted(CommonConstants.YES);
            conversation.setIsPinned(CommonConstants.YES);
            conversation.setCommonParams();

            insertConversationList.add(conversation);
        }

        conversationMapper.insertConversationBatch(insertConversationList);

        existConversationList.addAll(insertConversationList);
        return existConversationList;
    }

    @Override
    public List<Conversation> selectConversationBydUidListAndRoomId(List<Long> userIdList, Long roomId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        if (CollectionUtil.isEmpty(userIdList)) {
            return new ArrayList<>();
        }

        return conversationMapper.selectConversationBydUidListAndRoomId(userIdList, roomId);
    }

    @Override
    public Conversation selectConversationBydUidAndRoomId(Long userId, Long roomId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");

        List<Conversation> conversationList = selectConversationBydUidListAndRoomId(Arrays.asList(userId), roomId);
        if (CollectionUtil.isEmpty(conversationList)) {
            return null;
        }
        return conversationList.get(0);
    }

    private void checkCreateConversationParams(ConversationCreateContext conversationContext) {
        AssertUtils.isNotEmpty(conversationContext, "参数不能为空");
        AssertUtils.isNotEmpty(conversationContext.getRoomId(), "房间ID不能为空");
        AssertUtils.isTrue(CollectionUtil.isNotEmpty(conversationContext.getUserIdList()), "用户ID列表不能为空");
    }
}
