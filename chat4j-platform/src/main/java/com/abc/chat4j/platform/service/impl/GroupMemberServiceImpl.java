package com.abc.chat4j.platform.service.impl;

import com.abc.chat4j.platform.domain.entity.GroupMember;
import com.abc.chat4j.platform.mapper.GroupMemberMapper;
import com.abc.chat4j.platform.service.GroupMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {
}
