package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.platform.domain.entity.RoomMember;
import com.abc.chat4j.platform.mapper.RoomMemberMapper;
import com.abc.chat4j.platform.service.RoomMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoomMemberServiceImpl extends ServiceImpl<RoomMemberMapper, RoomMember> implements RoomMemberService {
}
