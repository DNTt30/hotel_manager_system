package com.duong.salesmanagement.controller;

import com.duong.salesmanagement.model.Guest;
import com.duong.salesmanagement.model.Reservation;
import com.duong.salesmanagement.service.GuestService;
import com.duong.salesmanagement.repository.ReservationRepository; // Đảm bảo có import này
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;
    private final ReservationRepository reservationRepository; // Khai báo thêm repository

    // Tiêm cả GuestService và ReservationRepository qua Constructor
    public GuestController(GuestService guestService, ReservationRepository reservationRepository) {
        this.guestService = guestService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/list")
    public String listGuests(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model, 
            HttpServletRequest request) {

        List<Guest> guests = (keyword != null && !keyword.isEmpty()) 
            ? guestService.searchGuest(keyword) 
            : guestService.getAllGuests();

        model.addAttribute("guests", guests);
        model.addAttribute("keyword", keyword);

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "guest/list :: #guestTable"; 
        }

        return "guest/list";
    }

    // PHƯƠNG THỨC XỬ LÝ LỖI 404 BẠN ĐANG GẶP
    @GetMapping("/details/{id}")
    public String showGuestDetails(@PathVariable("id") Long id, Model model) {
        Guest guest = guestService.getGuestById(id);
        // Lấy lịch sử từ bảng Reservation dựa trên Guest ID
        List<Reservation> history = reservationRepository.findByGuestGuestId(id);

        model.addAttribute("guest", guest);
        model.addAttribute("history", history);
        return "guest/detail"; // Tên file templates/guest/detail.html
    }

    @GetMapping("/add")
    public String showAddGuestForm(Model model) {
        model.addAttribute("guest", new Guest());
        return "guest/add";
    }

    @PostMapping("/save")
    public String saveGuest(@ModelAttribute("guest") Guest guest) {
        if (guest.getGuestId() == null) {
            guestService.createGuest(guest);
        } else {
            guestService.updateGuest(guest.getGuestId(), guest);
        }
        return "redirect:/guests/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("guest", guestService.getGuestById(id));
        return "guest/add";
    }

    @GetMapping("/delete/{id}")
    public String deleteGuest(@PathVariable("id") Long id) {
        guestService.deleteGuest(id);
        return "redirect:/guests/list";
    }
}