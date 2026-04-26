package com.abc.chat4j.common.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResult {

    private List<?> list;

    private Long total;

}
