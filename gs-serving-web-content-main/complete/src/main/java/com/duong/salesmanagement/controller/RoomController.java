package com.duong.salesmanagement.controller;

import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.repository.RoomTypeRepository;
import com.duong.salesmanagement.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final RoomTypeRepository roomTypeRepository;

    public RoomController(RoomService roomService, RoomTypeRepository roomTypeRepository) {
        this.roomService = roomService;
        this.roomTypeRepository = roomTypeRepository;
    }

    // 🔹 LIST + SEARCH + AJAX
    @GetMapping("/list")
    public String listRooms(
            @RequestParam(required = false) String search,
            Model model) {
        model.addAttribute("rooms", roomService.search(search));
        model.addAttribute("keyword", search);
        return "room/list";
    }

    // 🔹 ADD
    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("roomTypes", roomTypeRepository.findAll());
        return "room/add";
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute Room room, @RequestParam("roomTypeId") Long roomTypeId) {
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("AVAILABLE");
        }
        // Set RoomType from ID
        roomTypeRepository.findById(roomTypeId).ifPresent(room::setRoomType);
        roomService.saveRoom(room);
        return "redirect:/rooms/list";
    }

    // 🔹 EDIT
    @GetMapping("/edit/{id}")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id));
        model.addAttribute("roomTypes", roomTypeRepository.findAll());
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
            @RequestParam String status) {
        roomService.updateRoomStatus(id, status);
        return "redirect:/rooms/list";
    }
}
