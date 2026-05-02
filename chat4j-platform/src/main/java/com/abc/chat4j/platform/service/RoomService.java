package com.abc.chat4j.platform.service;

import com.abc.chat4j.platform.domain.context.RoomCreateContext;
import com.abc.chat4j.platform.domain.entity.Room;
import com.abc.chat4j.platform.domain.vo.RoomInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoomService extends IService<Room> {
    Room selectRoomByRoomId(Long roomId);

    List<Room> selectRoomListByRoomIds(List<Long> roomIds);

    void checkUserInRoom(Long userId, Long roomId);

    List<RoomInfoVO> selectRoomInfoVOListByRoomIds(List<Long> roomIdList);

    List<Long> getRoomMemberListByRoomId(Long roomId);

    RoomInfoVO createRoom(RoomCreateContext context);
}
