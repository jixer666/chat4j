package com.abc.chat4j.platform.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.abc.chat4j.platform.mapper.PrivateRoomMapper;
import com.abc.chat4j.platform.service.PrivateRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PrivateRoomServiceImpl extends ServiceImpl<PrivateRoomMapper, PrivateRoom> implements PrivateRoomService {

    @Autowired
    private PrivateRoomMapper privateRoomMapper;

    @Override
    public PrivateRoom selectPrivateRoomByRoomId(Long roomId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        return privateRoomMapper.selectByRoomId(roomId);
    }

    @Override
    public List<PrivateRoom> selectPrivateRoomListByRoomIds(List<Long> roomIds) {
        if (CollectionUtil.isEmpty(roomIds)) {
            return new ArrayList<>();
        }
        return privateRoomMapper.selectPrivateRoomListByRoomIds(roomIds);
    }
}
