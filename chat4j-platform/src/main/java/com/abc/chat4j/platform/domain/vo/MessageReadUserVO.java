package com.abc.chat4j.platform.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author LiJunXi
 * @date 2026/5/6
 */
@Data
public class MessageReadUserVO {

    private List<ImUserVO> readUserList;

    private List<ImUserVO> unReadUserList;

}
