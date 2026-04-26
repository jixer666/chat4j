package com.abc.chat4j.system.mapper;

import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.system.domain.dto.UserDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> selectUserList(UserDTO userDTO);
}
