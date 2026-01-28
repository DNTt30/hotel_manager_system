package com.duong.salesmanagement.repository;

import com.duong.salesmanagement.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    // Thêm hàm này để hết lỗi existsByEmail
    boolean existsByEmail(String email);

    // Thêm hàm này để hết lỗi findByFullNameContainingIgnoreCase
    List<Guest> findByFullNameContainingIgnoreCase(String fullName);

    // Tìm kiếm theo tên hoặc email
    List<Guest> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String fullName, String email);
}