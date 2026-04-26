package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.platform.domain.entity.Room;
import com.abc.chat4j.platform.mapper.RoomMapper;
import com.abc.chat4j.platform.service.RoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {
}
