package com.abc.chat4j.system.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DictVO {

    private Long dictId;

    private String dictName;

    private String dictKey;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
