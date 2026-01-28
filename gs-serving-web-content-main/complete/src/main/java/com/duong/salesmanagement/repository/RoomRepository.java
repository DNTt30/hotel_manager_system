package com.duong.salesmanagement.repository;

import com.duong.salesmanagement.model.Room;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // 🔥 FETCH JOIN để load roomType
    @Query("""
                SELECT r FROM Room r
                LEFT JOIN FETCH r.roomType rt
                WHERE (:keyword IS NULL
                   OR r.roomNumber LIKE %:keyword%
                   OR rt.typeName LIKE %:keyword%)
            """)
    List<Room> search(@Param("keyword") String keyword);

    @Query("""
                SELECT r FROM Room r
                LEFT JOIN FETCH r.roomType
                WHERE r.status = :status
                ORDER BY r.roomId ASC
            """)
    List<Room> findTop20ByStatus(@Param("status") String status);

    // Lấy danh sách phòng theo trạng thái
    List<Room> findByStatus(String status);
}
