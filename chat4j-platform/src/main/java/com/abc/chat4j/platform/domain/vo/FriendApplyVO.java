package com.abc.chat4j.platform.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author LiJunXi
 * @date 2026/5/1
 */
@Data
public class FriendApplyVO {

    private Long friendApplyId;

    private Long userId;

    private Long friendId;

    private String remark;

    private Integer status;

    private Date createTime;

    // 请求方用户信息
    private ImUserVO userInfo;

    // 同意申请才会有值
    private ConversationVO conversation;

}
