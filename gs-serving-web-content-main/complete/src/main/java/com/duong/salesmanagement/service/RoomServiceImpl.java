package com.duong.salesmanagement.service;

import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room getRoomById(Long id) {
        if (id == null) return null;
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public void updateRoomStatus(Long id, String status) {
        Room room = getRoomById(id);
        if (room != null) {
            room.setStatus(status);
            roomRepository.save(room);
        }
    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatus("AVAILABLE");
    }

    // SỬA LỖI NULL SAFETY TẠI ĐÂY
    @Override
    public void saveRoom(Room room) {
        // Kiểm tra nếu đối tượng room bị null thì ném lỗi hoặc bỏ qua
        if (room == null) {
            throw new IllegalArgumentException("Room object cannot be null");
        }
        roomRepository.save(room);
    }
}