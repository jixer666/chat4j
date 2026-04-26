package com.abc.chat4j.system.convert;

import cn.hutool.core.bean.BeanUtil;
import com.abc.chat4j.common.constant.CommonConstants;
import com.abc.chat4j.common.util.IdUtils;
import com.abc.chat4j.system.domain.dto.RegisterDTO;
import com.abc.chat4j.common.domain.entity.User;
import com.abc.chat4j.system.domain.vo.RoleVO;
import com.abc.chat4j.system.domain.vo.UserRoleVO;
import com.abc.chat4j.system.domain.vo.UserVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

public class UserConvert {

    public static User convertToUserByRegisterDTO(RegisterDTO registerDTO) {
        User user = User.builder()
                .username(registerDTO.getUsername())
                .password(new BCryptPasswordEncoder().encode(registerDTO.getPassword()))
                .nickname(CommonConstants.DEFAULT_NICKNAME)
                .avatar(CommonConstants.DEFAULT_AVATAR)
                .build();
        user.setUserId(IdUtils.getId());
        user.setCommonParams();

        return user;
    }

    public static UserVO convertToUserVO(User user, List<String> roleKeys) {
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        userVO.setRoles(roleKeys);
        return userVO;
    }

    public static UserRoleVO buildUserRoleVO(List<?> roles, List<Long> roleIds) {
        return UserRoleVO.builder()
                .roleIds(roleIds)
                .roles((List<RoleVO>) roles)
                .build();
    }
}
