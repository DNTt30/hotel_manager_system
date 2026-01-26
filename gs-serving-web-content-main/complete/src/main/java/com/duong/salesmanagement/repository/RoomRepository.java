package com.duong.salesmanagement.repository;

import com.duong.salesmanagement.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // Thêm hàm này để tìm các phòng có trạng thái nhất định
    List<Room> findByStatus(String status);
}