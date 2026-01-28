package com.duong.salesmanagement.controller.api;

import com.duong.salesmanagement.model.Room;
import com.duong.salesmanagement.model.RoomType;
import com.duong.salesmanagement.repository.RoomRepository;
import com.duong.salesmanagement.repository.RoomTypeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Room", description = "API quản lý phòng")
public class RoomApiController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Operation(summary = "Lấy danh sách tất cả phòng")
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    @Operation(summary = "Lấy thông tin phòng theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy phòng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy phòng")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lấy danh sách phòng trống")
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> availableRooms = roomRepository.findByStatus("AVAILABLE");
        return ResponseEntity.ok(availableRooms);
    }

    @Operation(summary = "Thêm phòng mới")
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room savedRoom = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    @Operation(summary = "Cập nhật thông tin phòng")
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Room room = optionalRoom.get();
        room.setRoomNumber(roomDetails.getRoomNumber());
        room.setStatus(roomDetails.getStatus());
        room.setRoomType(roomDetails.getRoomType());
        return ResponseEntity.ok(roomRepository.save(room));
    }

    @Operation(summary = "Cập nhật trạng thái phòng")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Room> updateRoomStatus(
            @PathVariable Long id,
            @Parameter(description = "Trạng thái mới: AVAILABLE, OCCUPIED, MAINTENANCE") @RequestParam String status) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Room room = optionalRoom.get();
        room.setStatus(status);
        return ResponseEntity.ok(roomRepository.save(room));
    }

    @Operation(summary = "Xóa phòng")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (!roomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
