package com.duong.salesmanagement.service;

import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.search(null);
    }

    @Override
    public Room getRoomById(Long id) {
        return id == null ? null : roomRepository.findById(id).orElse(null);
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
        return roomRepository.findTop20ByStatus("AVAILABLE");
    }

    @Override
    public void saveRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        roomRepository.save(room);
    }

    @Override
    public void deleteRoomById(Long id) {
        if (id != null) {
            roomRepository.deleteById(id);
        }
    }

    @Override
    public List<Room> search(String keyword) {
        return roomRepository.search(keyword);
    }
}
