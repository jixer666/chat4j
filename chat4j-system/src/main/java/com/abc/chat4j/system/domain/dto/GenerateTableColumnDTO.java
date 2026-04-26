package com.abc.chat4j.system.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenerateTableColumnDTO {

    private Long genTableColumnId;

    private List<Long> genTableColumnIds;


    public void checkSaveParams() {
    }

    public void checkDeleteParams() {
    }

    public void checkUpdateParams() {
    }

}
