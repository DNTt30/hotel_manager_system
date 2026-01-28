package com.duong.salesmanagement.controller.api;

import com.duong.salesmanagement.model.Reservation;
import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.repository.ReservationRepository;
import com.duong.salesmanagement.repository.RoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservation", description = "API quản lý đặt phòng")
public class ReservationApiController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Operation(summary = "Lấy danh sách tất cả đặt phòng")
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @Operation(summary = "Lấy chi tiết đặt phòng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tạo đặt phòng mới")
    @ApiResponse(responseCode = "201", description = "Đặt phòng thành công")
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        reservation.setCreatedDate(LocalDate.now());
        reservation.setStatus("PENDING");
        reservation.setConfirmationNumber("CNF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Reservation saved = reservationRepository.save(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Cập nhật đặt phòng")
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation details) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reservation reservation = optional.get();
        reservation.setCheckInDate(details.getCheckInDate());
        reservation.setCheckOutDate(details.getCheckOutDate());
        reservation.setNumberOfGuests(details.getNumberOfGuests());
        reservation.setSpecialRequests(details.getSpecialRequests());
        reservation.setTotalPrice(details.getTotalPrice());
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @Operation(summary = "Check-in khách")
    @PatchMapping("/{id}/check-in")
    public ResponseEntity<Reservation> checkIn(@PathVariable Long id) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reservation reservation = optional.get();
        reservation.setStatus("CHECKED_IN");
        // Cập nhật trạng thái phòng
        Room room = reservation.getRoom();
        if (room != null) {
            room.setStatus("OCCUPIED");
            roomRepository.save(room);
        }
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @Operation(summary = "Check-out khách")
    @PatchMapping("/{id}/check-out")
    public ResponseEntity<Reservation> checkOut(@PathVariable Long id) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reservation reservation = optional.get();
        reservation.setStatus("CHECKED_OUT");
        // Cập nhật trạng thái phòng
        Room room = reservation.getRoom();
        if (room != null) {
            room.setStatus("AVAILABLE");
            roomRepository.save(room);
        }
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @Operation(summary = "Hủy đặt phòng")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        Optional<Reservation> optional = reservationRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reservation reservation = optional.get();
        reservation.setStatus("CANCELLED");
        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @Operation(summary = "Xóa đặt phòng")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (!reservationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
