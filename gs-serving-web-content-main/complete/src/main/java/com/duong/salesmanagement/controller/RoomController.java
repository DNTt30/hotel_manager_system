package com.duong.salesmanagement.controller;

import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Hiển thị danh sách/sơ đồ phòng
    @GetMapping("/list")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "room/list";
    }

    // --- PHƯƠNG THỨC GIẢI QUYẾT LỖI 404 CỦA BẠN ---
    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "room/add"; // Tìm tệp tại templates/room/add.html
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute("room") Room room) {
        // Mặc định phòng mới là AVAILABLE nếu chưa chọn
        if (room.getStatus() == null || room.getStatus().isEmpty()) {
            room.setStatus("AVAILABLE");
        }
        roomService.saveRoom(room);
        return "redirect:/rooms/list";
    }

    @GetMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        roomService.updateRoomStatus(id, status);
        return "redirect:/rooms/list";
    }
}