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
            @RequestParam("check_in_date") String checkInDate,
            @RequestParam("check_out_date") String checkOutDate,
            @RequestParam(value = "confirmation_number", required = false) String confirmationNumber,
            @RequestParam(value = "status", required = false) String status) {

        Reservation reservation = new Reservation();

        // Gán Guest và Room
        Guest guest = guestRepository.findById(guestId).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        reservation.setGuest(guest);
        reservation.setRoom(room);

        // Parse và set dates
        reservation.setCheck_in_date(LocalDate.parse(checkInDate));
        reservation.setCheck_out_date(LocalDate.parse(checkOutDate));

        // Set created date
        reservation.setCreated_date(LocalDate.now());

        // Set confirmation number
        if (confirmationNumber != null && !confirmationNumber.isEmpty()) {
            reservation.setConfirmation_number(confirmationNumber);
        } else {
            // Auto-generate confirmation number
            reservation.setConfirmation_number("CNF-" + System.currentTimeMillis());
        }

        // Set status
        if (status != null && !status.isEmpty()) {
            reservation.setStatus(status);
        } else {
            reservation.setStatus("PENDING");
        }

        reservationRepository.save(reservation);
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
    public String updateReservation(@RequestParam("reservationId") int reservationId,
            @RequestParam("guestId") Long guestId,
            @RequestParam("roomId") Long roomId,
            @RequestParam("check_in_date") String checkInDate,
            @RequestParam("check_out_date") String checkOutDate,
            @RequestParam(value = "confirmation_number", required = false) String confirmationNumber,
            @RequestParam(value = "status", required = false) String status) {

        Reservation reservation = reservationRepository.findById((long) reservationId).orElse(null);
        if (reservation != null) {
            Guest guest = guestRepository.findById(guestId).orElse(null);
            Room room = roomRepository.findById(roomId).orElse(null);

            reservation.setGuest(guest);
            reservation.setRoom(room);
            reservation.setCheck_in_date(LocalDate.parse(checkInDate));
            reservation.setCheck_out_date(LocalDate.parse(checkOutDate));
            reservation.setConfirmation_number(confirmationNumber);
            reservation.setStatus(status);

            reservationRepository.save(reservation);
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
