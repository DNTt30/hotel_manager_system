package com.duong.salesmanagement.controller;

import com.duong.salesmanagement.model.Guest;
import com.duong.salesmanagement.service.GuestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @GetMapping("/list")
    public String listGuests(Model model) {
        model.addAttribute("guests", guestService.getAllGuests());
        return "guest/list";
    }

    @GetMapping("/add")
    public String showAddGuestForm(Model model) {
        model.addAttribute("guest", new Guest());
        return "guest/add";
    }

    @PostMapping("/save")
    public String saveGuest(@ModelAttribute("guest") Guest guest) {
        guestService.createGuest(guest);
        return "redirect:/guests/list";
    }
}
