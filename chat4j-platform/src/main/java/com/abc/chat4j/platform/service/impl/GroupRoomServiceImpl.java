package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.platform.domain.entity.GroupRoom;
import com.abc.chat4j.platform.mapper.GroupRoomMapper;
import com.abc.chat4j.platform.service.GroupRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GroupRoomServiceImpl extends ServiceImpl<GroupRoomMapper, GroupRoom> implements GroupRoomService {
}
