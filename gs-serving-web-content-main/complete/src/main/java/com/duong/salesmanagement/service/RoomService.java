package com.duong.salesmanagement.service;

import com.duong.salesmanagement.model.Room;
import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    void updateRoomStatus(Long id, String status);
    List<Room> getAvailableRooms();
    
    // Khai báo hàm saveRoom
    void saveRoom(Room room);
}