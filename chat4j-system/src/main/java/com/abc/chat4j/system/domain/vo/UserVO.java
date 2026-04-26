package com.abc.chat4j.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {

    private String userId;

    private String username;

    private String avatar;

    private String nickname;

    private List<String> roles;

    private Date createTime;

    private Date updateTime;

    private Integer status;

}
