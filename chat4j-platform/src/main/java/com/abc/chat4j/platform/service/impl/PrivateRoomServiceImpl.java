package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.common.util.AssertUtils;
import com.abc.chat4j.platform.domain.entity.PrivateRoom;
import com.abc.chat4j.platform.mapper.PrivateRoomMapper;
import com.abc.chat4j.platform.service.PrivateRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivateRoomServiceImpl extends ServiceImpl<PrivateRoomMapper, PrivateRoom> implements PrivateRoomService {

    @Autowired
    private PrivateRoomMapper privateRoomMapper;

    @Override
    public PrivateRoom selectPrivateRoomByRoomId(Long roomId) {
        AssertUtils.isNotEmpty(roomId, "房间ID不能为空");
        return privateRoomMapper.selectByRoomId(roomId);
    }
}
