package com.duong.salesmanagement.controller;

import com.duong.salesmanagement.model.Reservation;
import com.duong.salesmanagement.model.Guest;
import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.repository.ReservationRepository;
import com.duong.salesmanagement.repository.GuestRepository;
import com.duong.salesmanagement.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Hiển thị danh sách đặt phòng
    @GetMapping("/list")
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationRepository.findAll();
        model.addAttribute("reservations", reservations);
        return "reservation/list";
    }

    // Hiển thị form thêm đặt phòng mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("guests", guestRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll());
        return "reservation/add";
    }

    // Lưu đặt phòng mới
    @PostMapping("/save")
    public String saveReservation(@RequestParam("guestId") Long guestId,
            @RequestParam("roomId") Long roomId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate,
            @RequestParam(value = "confirmationNumber", required = false) String confirmationNumber,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "numberOfGuests", required = false, defaultValue = "1") int numberOfGuests,
            @RequestParam(value = "specialRequests", required = false) String specialRequests,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        // Parse dates first
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);

        // Validation 1: Check-out date phải sau Check-in date
        if (!checkOut.isAfter(checkIn)) {
            redirectAttributes.addFlashAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng!");
            return "redirect:/reservations/add";
        }

        // Validation 2: Kiểm tra phòng không bị trùng lịch
        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(roomId, checkIn, checkOut);
        if (!overlapping.isEmpty()) {
            Room room = roomRepository.findById(roomId).orElse(null);
            String roomNumber = room != null ? room.getRoomNumber() : "N/A";
            redirectAttributes.addFlashAttribute("error",
                    String.format(
                            "Phòng %s đã được đặt trong khoảng thời gian này! Vui lòng chọn ngày khác hoặc phòng khác.",
                            roomNumber));
            return "redirect:/reservations/add";
        }

        Reservation reservation = new Reservation();

        // Gán Guest và Room
        Guest guest = guestRepository.findById(guestId).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        reservation.setGuest(guest);
        reservation.setRoom(room);

        // Set dates
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);

        // Set created date
        reservation.setCreatedDate(LocalDate.now());

        // Set confirmation number
        if (confirmationNumber != null && !confirmationNumber.isEmpty()) {
            reservation.setConfirmationNumber(confirmationNumber);
        } else {
            // Auto-generate confirmation number
            reservation.setConfirmationNumber("CNF-" + System.currentTimeMillis());
        }

        // Set status
        if (status != null && !status.isEmpty()) {
            reservation.setStatus(status);
        } else {
            reservation.setStatus("PENDING");
        }

        // Set additional fields
        reservation.setNumberOfGuests(numberOfGuests);
        reservation.setSpecialRequests(specialRequests);

        // Calculate total price based on room type and number of nights
        if (room != null && room.getRoomType() != null) {
            long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
            if (nights > 0) {
                BigDecimal pricePerNight = room.getRoomType().getBasePrice();
                reservation.setTotalPrice(pricePerNight.multiply(BigDecimal.valueOf(nights)));
            }
        }

        reservationRepository.save(reservation);
        redirectAttributes.addFlashAttribute("success", "Đặt phòng thành công!");
        return "redirect:/reservations/list";
    }

    // Xem chi tiết đặt phòng
    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable("id") Long id, Model model) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        model.addAttribute("reservation", reservation);
        return "reservation/detail";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        model.addAttribute("reservation", reservation);
        model.addAttribute("guests", guestRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll());
        return "reservation/edit";
    }

    // Cập nhật đặt phòng
    @PostMapping("/update")
    public String updateReservation(@RequestParam("reservationId") Long reservationId,
            @RequestParam("guestId") Long guestId,
            @RequestParam("roomId") Long roomId,
            @RequestParam("checkInDate") String checkInDate,
            @RequestParam("checkOutDate") String checkOutDate,
            @RequestParam(value = "confirmationNumber", required = false) String confirmationNumber,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "numberOfGuests", required = false, defaultValue = "1") int numberOfGuests,
            @RequestParam(value = "specialRequests", required = false) String specialRequests,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        // Parse dates first
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);

        // Validation 1: Check-out date phải sau Check-in date
        if (!checkOut.isAfter(checkIn)) {
            redirectAttributes.addFlashAttribute("error", "Ngày trả phòng phải sau ngày nhận phòng!");
            return "redirect:/reservations/edit/" + reservationId;
        }

        // Validation 2: Kiểm tra phòng không bị trùng lịch (loại trừ reservation đang
        // cập nhật)
        List<Reservation> overlapping = reservationRepository.findOverlappingReservationsExcluding(
                roomId, checkIn, checkOut, reservationId);
        if (!overlapping.isEmpty()) {
            Room room = roomRepository.findById(roomId).orElse(null);
            String roomNumber = room != null ? room.getRoomNumber() : "N/A";
            redirectAttributes.addFlashAttribute("error",
                    String.format(
                            "Phòng %s đã được đặt trong khoảng thời gian này! Vui lòng chọn ngày khác hoặc phòng khác.",
                            roomNumber));
            return "redirect:/reservations/edit/" + reservationId;
        }

        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);
        if (reservation != null) {
            Guest guest = guestRepository.findById(guestId).orElse(null);
            Room room = roomRepository.findById(roomId).orElse(null);

            reservation.setGuest(guest);
            reservation.setRoom(room);
            reservation.setCheckInDate(checkIn);
            reservation.setCheckOutDate(checkOut);
            reservation.setConfirmationNumber(confirmationNumber);
            reservation.setStatus(status);
            reservation.setNumberOfGuests(numberOfGuests);
            reservation.setSpecialRequests(specialRequests);

            // Recalculate total price
            if (room != null && room.getRoomType() != null) {
                long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
                if (nights > 0) {
                    BigDecimal pricePerNight = room.getRoomType().getBasePrice();
                    reservation.setTotalPrice(pricePerNight.multiply(BigDecimal.valueOf(nights)));
                }
            }

            reservationRepository.save(reservation);
            redirectAttributes.addFlashAttribute("success", "Cập nhật đặt phòng thành công!");
        }
        return "redirect:/reservations/details/" + reservationId;
    }

    // Xóa đặt phòng
    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable("id") Long id) {
        reservationRepository.deleteById(id);
        return "redirect:/reservations/list";
    }

    // Cập nhật trạng thái đặt phòng
    @GetMapping("/update-status/{id}")
    public String updateStatus(@PathVariable("id") Long id, @RequestParam("status") String status) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation != null) {
            reservation.setStatus(status);
            reservationRepository.save(reservation);
        }
        return "redirect:/reservations/details/" + id;
    }
}
