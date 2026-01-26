package com.duong.salesmanagement.controller;

import com.duong.salesmanagement.model.Guest;
import com.duong.salesmanagement.service.GuestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
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

        // Xử lý AJAX cho tính năng tự động tìm kiếm
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "guest/list :: #guestTable"; 
        }

        return "guest/list";
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

    // --- BỔ SUNG CÁC MAPPING CÒN THIẾU ---
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("guest", guestService.getGuestById(id));
        return "guest/add"; // Dùng chung form với trang Add
    }

    @GetMapping("/delete/{id}")
    public String deleteGuest(@PathVariable("id") Long id) {
        guestService.deleteGuest(id);
        return "redirect:/guests/list";
    }
}