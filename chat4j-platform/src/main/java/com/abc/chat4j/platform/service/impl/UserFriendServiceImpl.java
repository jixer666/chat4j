package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.common.util.DateUtils;
import com.abc.chat4j.common.util.SecurityUtils;
import com.abc.chat4j.platform.constant.ImConstant;
import com.abc.chat4j.platform.domain.context.UserFriendQueryContext;
import com.abc.chat4j.platform.domain.dto.UserFriendPullDTO;
import com.abc.chat4j.platform.domain.entity.UserFriend;
import com.abc.chat4j.platform.domain.vo.ImUserVO;
import com.abc.chat4j.platform.domain.vo.UserFriendVO;
import com.abc.chat4j.platform.mapper.UserFriendMapper;
import com.abc.chat4j.platform.service.UserFriendService;
import com.abc.chat4j.system.cache.UserCache;
import com.abc.chat4j.system.domain.dto.UserDTO;
import com.abc.chat4j.system.domain.vo.UserVO;
import com.abc.chat4j.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserFriendServiceImpl extends ServiceImpl<UserFriendMapper, UserFriend> implements UserFriendService {

    @Autowired
    private UserFriendMapper userFriendMapper;

    @Autowired
    private UserCache userCache;

    @Autowired
    private UserService userService;

    @Override
    public UserFriend selectUserFriendByUidAndFriendId(Long userId, Long friendId) {
        AssertUtils.isNotEmpty(userId, "用户ID不能为空");
        AssertUtils.isNotEmpty(friendId, "好友ID不能为空");
        return userFriendMapper.selectByUidAndFriendId(userId, friendId);
    }

    @Override
    public List<ImUserVO> selectOfflineUserFriendList(UserFriendPullDTO userFriendPullDTO) {
        checkUserFriendPullDTOParams(userFriendPullDTO);

        UserFriendQueryContext context = new UserFriendQueryContext();
        context.setUserId(SecurityUtils.getUserId());
        context.setMinUpdateTime(userFriendPullDTO.getMinUpdateTime());
        List<UserFriend> messageList = selectUserFriend(context);

        List<Long> userIdSet = messageList.stream().map(UserFriend::getFriendId).collect(Collectors.toList());
        Map<Long, User> userMap = userCache.getBatch(userIdSet);

        return messageList.stream().map(item -> buildImUserVO(item, userMap)).collect(Collectors.toList());
    }

    private ImUserVO buildImUserVO(UserFriend userFriend, Map<Long, User> userMap) {
        User user = userMap.get(userFriend.getFriendId());
        ImUserVO imUserVO = new ImUserVO();
        imUserVO.setUserId(user.getUserId());
        imUserVO.setUsername(user.getUsername());
        imUserVO.setAvatar(user.getAvatar());
        imUserVO.setNickname(user.getNickname());
        return imUserVO;
    }

    private List<UserFriend> selectUserFriend(UserFriendQueryContext context) {
        return userFriendMapper.selectUserFriendList(context);
    }

    private void checkUserFriendPullDTOParams(UserFriendPullDTO userFriendPullDTO) {
        AssertUtils.isNotEmpty(userFriendPullDTO, "参数不能为空");
        // 好友列表最大取30天内的
        Date minUpdateTime = userFriendPullDTO.getMinUpdateTime();
        Date maxMinUpdateTime = DateUtils.addDays(new Date(), Math.toIntExact(-ImConstant.MAX_OFFLINE_USER_FRIEND_DAYS));
        userFriendPullDTO.setMinUpdateTime(Objects.isNull(minUpdateTime) ? maxMinUpdateTime :
                minUpdateTime.before(maxMinUpdateTime) ? minUpdateTime : maxMinUpdateTime);
    }

    @Override
    public UserFriendVO searchUserFriend(UserDTO userDTO) {
        checkSearchUserFriendParams(userDTO);
        User user = userService.getUserByUsername(userDTO.getUsername());
        if (Objects.isNull(user)) {
            return null;
        }
        UserFriend userFriend = selectUserFriendByUidAndFriendId(SecurityUtils.getUserId(), user.getUserId());

        UserFriendVO  userFriendVO = new UserFriendVO();
        userFriendVO.setUserId(user.getUserId());
        userFriendVO.setNickname(user.getNickname());
        userFriendVO.setAvatar(user.getAvatar());
        userFriendVO.setIsFriend(Objects.nonNull(userFriend));

        return userFriendVO;
    }

    private void checkSearchUserFriendParams(UserDTO userDTO) {
        AssertUtils.isNotEmpty(userDTO, "参数不能为空");
        AssertUtils.isNotEmpty(userDTO.getUsername(), "账号不能为空");
    }
}
