package com.duong.salesmanagement.repository;

import com.duong.salesmanagement.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Hàm này dùng để lấy lịch sử lưu trú của khách hàng theo ID
    List<Reservation> findByGuestGuestId(Long guestId);

    /**
     * Kiểm tra xem phòng có bị trùng lịch đặt không
     * Overlap xảy ra khi: checkIn mới < checkOut cũ AND checkOut mới > checkIn cũ
     * Loại trừ các reservation đã CANCELLED
     */
    @Query("SELECT r FROM Reservation r WHERE r.room.roomId = :roomId " +
            "AND r.status != 'CANCELLED' " +
            "AND r.checkInDate < :checkOutDate " +
            "AND r.checkOutDate > :checkInDate")
    List<Reservation> findOverlappingReservations(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate);

    /**
     * Kiểm tra trùng lịch nhưng loại trừ reservation đang cập nhật
     */
    @Query("SELECT r FROM Reservation r WHERE r.room.roomId = :roomId " +
            "AND r.reservationId != :excludeId " +
            "AND r.status != 'CANCELLED' " +
            "AND r.checkInDate < :checkOutDate " +
            "AND r.checkOutDate > :checkInDate")
    List<Reservation> findOverlappingReservationsExcluding(
            @Param("roomId") Long roomId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("excludeId") Long excludeId);
}