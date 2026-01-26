package com.duong.salesmanagement.repository;

import com.duong.salesmanagement.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Hàm này dùng để lấy lịch sử lưu trú của khách hàng theo ID
    List<Reservation> findByGuestGuestId(Long guestId);
}