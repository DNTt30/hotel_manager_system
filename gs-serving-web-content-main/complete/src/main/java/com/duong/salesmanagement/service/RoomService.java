package com.duong.salesmanagement.service;

import com.duong.salesmanagement.model.Room;
import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    void updateRoomStatus(Long id, String status);
    void saveRoom(Room room);
    void deleteRoomById(Long id);
    
    // Đảm bảo tên hàm ở đây là getAvailableRooms
    List<Room> getAvailableRooms(); 
    List<Room> search(String keyword);
}