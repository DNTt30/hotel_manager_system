package com.duong.salesmanagement.controller;

import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // 🔹 LIST + SEARCH + AJAX
    @GetMapping("/list")
    public String listRooms(
            @RequestParam(required = false) String search,
            Model model
    ) {
        model.addAttribute("rooms", roomService.search(search));
        model.addAttribute("keyword", search);
        return "room/list";
    }

    // 🔹 ADD
    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room/add";
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room) {
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("AVAILABLE");
        }
        roomService.saveRoom(room);
        return "redirect:/rooms/list";
    }

    // 🔹 EDIT
    @GetMapping("/edit/{id}")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id));
        return "room/edit";
    }

    // 🔹 DELETE
    @GetMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoomById(id);
        return "redirect:/rooms/list";
    }

    // 🔹 UPDATE STATUS
    @GetMapping("/update-status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        roomService.updateRoomStatus(id, status);
        return "redirect:/rooms/list";
    }
}
