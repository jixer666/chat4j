package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.context.FriendApplyQueryContext;
import com.abc.chat4j.platform.domain.context.MessageQueryContext;
import com.abc.chat4j.platform.domain.dto.FriendApplyDTO;
import com.abc.chat4j.platform.domain.dto.FriendApplyPullDTO;
import com.abc.chat4j.platform.domain.entity.FriendApply;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.domain.enums.FriendApplyStatusEnum;
import com.abc.chat4j.platform.domain.vo.FriendApplyVO;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.mapper.FriendApplyMapper;
import com.abc.chat4j.platform.service.FriendApplyService;
import com.abc.chat4j.system.cache.UserCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FriendApplyServiceImpl extends ServiceImpl<FriendApplyMapper, FriendApply> implements FriendApplyService {

    @Autowired
    private FriendApplyMapper friendApplyMapper;

    @Autowired
    private UserCache userCache;

    @Override
    public FriendApplyVO sendFriendApply(FriendApplyDTO friendApplyDTO) {
        FriendApply friendApply = saveOrUpdateUserFriendApply(friendApplyDTO);
        sendFriendApplyMessage(friendApply);
        return BeanUtil.copyProperties(friendApply, FriendApplyVO.class);
    }

    private void sendFriendApplyMessage(FriendApply friendApply) {

    }

    private void checkUserFriendApplyDTOParams(FriendApplyDTO friendApplyDTO) {
        AssertUtils.isNotEmpty(friendApplyDTO, "好友请求参数不能为空");
        AssertUtils.isNotEmpty(friendApplyDTO.getFriendId(), "好友ID不能为空");
    }

    private FriendApply saveOrUpdateUserFriendApply(FriendApplyDTO friendApplyDTO) {
        checkUserFriendApplyDTOParams(friendApplyDTO);

        FriendApply friendApply = new FriendApply();
        FriendApply findUserApply = selectFriendApplyByUserIdAndFriendId(friendApplyDTO.getUserId(), friendApplyDTO.getFriendId());
        if (Objects.nonNull(findUserApply)) {
            AssertUtils.isFalse(FriendApplyStatusEnum.PENDING.getStatus().equals(findUserApply.getStatus()), "已发送好友申请，请勿重复");
            AssertUtils.isFalse(FriendApplyStatusEnum.AGREE.getStatus().equals(findUserApply.getStatus()), "已是好友关系，请勿重复");
            // 已拒绝的申请变为待处理
            friendApply.setStatus(FriendApplyStatusEnum.PENDING.getStatus());
            friendApplyMapper.updateById(friendApply);
        } else {
            friendApply.setUserId(friendApplyDTO.getUserId());
            friendApply.setFriendId(friendApplyDTO.getFriendId());
            friendApply.setRemark(friendApplyDTO.getRemark());
            friendApply.setCommonParams();
            friendApplyMapper.insert(friendApply);
        }

        return friendApply;
    }

    @Override
    public FriendApply selectFriendApplyByUserIdAndFriendId(Long userId, Long friendId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isNotEmpty(userId, "好友ID不能为空");

        FriendApplyQueryContext context = new FriendApplyQueryContext();
        context.setUserId(userId);
        context.setFriendId(friendId);

        return selectFriendApply(context).get(0);
    }

    @Override
    public List<FriendApplyVO> selectOfflineFriendApplyList(FriendApplyPullDTO friendApplyPullDTO) {
        checkFriendApplyPullDTOParams(friendApplyPullDTO);

        FriendApplyQueryContext context = new FriendApplyQueryContext();
        // 查询自己申请的好友申请
        context.setUserId(SecurityUtils.getUserId());
        context.setMinUpdateTime(friendApplyPullDTO.getMinUpdateTime());
        List<FriendApply> friendApplyList = selectFriendApply(context);
        // 查询别人申请，申请对方是自己的好友申请
        context.setFriendId(SecurityUtils.getUserId());
        context.setMinUpdateTime(friendApplyPullDTO.getMinUpdateTime());
        friendApplyList.addAll(selectFriendApply(context));

        return buildFriendApplyVOList(friendApplyList);
    }

    private List<FriendApplyVO> buildFriendApplyVOList(List<FriendApply> friendApplyList) {
        Set<Long> userIdSet = friendApplyList.stream().map(item -> SecurityUtils.getUserId().equals(item.getUserId()) ? item.getFriendId() : item.getUserId()).collect(Collectors.toSet());
        Map<Long, User> userMap = userCache.getBatch(new ArrayList<>(userIdSet));
        return friendApplyList.stream().map(item -> buildfriendApplyVO(item, userMap)).collect(Collectors.toList());
    }

    private FriendApplyVO buildfriendApplyVO(FriendApply friendApply, Map<Long, User> userMap) {
        FriendApplyVO friendApplyVO = new FriendApplyVO();
        friendApplyVO.setUserId(friendApply.getUserId());
        friendApplyVO.setFriendId(friendApply.getFriendId());
        friendApplyVO.setRemark(friendApply.getRemark());
        friendApplyVO.setFriendApplyId(friendApply.getFriendApplyId());
        friendApplyVO.setStatus(friendApply.getStatus());
        friendApplyVO.setCreateTime(friendApply.getCreateTime());

        User user = userMap.get(SecurityUtils.getUserId().equals(friendApply.getUserId()) ? friendApply.getFriendId() : friendApply.getUserId());
        ImUserVO imUserVO = new ImUserVO();
        imUserVO.setUserId(user.getUserId());
        imUserVO.setUsername(user.getUsername());
        imUserVO.setAvatar(user.getAvatar());
        imUserVO.setNickname(user.getNickname());
        friendApplyVO.setUserInfo(imUserVO);

        return friendApplyVO;
    }

    private List<FriendApply> selectFriendApply(FriendApplyQueryContext context) {
        return friendApplyMapper.selectFriendApply(context);
    }

    private void checkFriendApplyPullDTOParams(FriendApplyPullDTO friendApplyPullDTO) {
        AssertUtils.isNotEmpty(friendApplyPullDTO, "参数不能为空");
        // 申请最大取30天内的
        Date minUpdateTime = friendApplyPullDTO.getMinUpdateTime();
        Date maxMinUpdateTime = DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_FRIEND_APPLY_DAYS));
        friendApplyPullDTO.setMinUpdateTime(Objects.isNull(minUpdateTime) ? maxMinUpdateTime :
                minUpdateTime.before(maxMinUpdateTime) ? minUpdateTime : maxMinUpdateTime);
    }
}
