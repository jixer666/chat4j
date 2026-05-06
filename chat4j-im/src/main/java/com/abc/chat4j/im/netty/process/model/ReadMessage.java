package com.abc.chat4j.im.netty.process.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadMessage {

    private Long userId;

    private Long roomId;

    private List<Long> msgIdList;

}
