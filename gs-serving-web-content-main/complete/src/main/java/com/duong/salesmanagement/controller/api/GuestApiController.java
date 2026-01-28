package com.duong.salesmanagement.controller.api;

import com.duong.salesmanagement.model.Guest;
import com.duong.salesmanagement.repository.GuestRepository;
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
@RequestMapping("/api/guests")
@Tag(name = "Guest", description = "API quản lý khách hàng")
public class GuestApiController {

    @Autowired
    private GuestRepository guestRepository;

    @Operation(summary = "Lấy danh sách tất cả khách hàng")
    @ApiResponse(responseCode = "200", description = "Thành công")
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        List<Guest> guests = guestRepository.findAll();
        return ResponseEntity.ok(guests);
    }

    @Operation(summary = "Lấy thông tin khách hàng theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy khách hàng"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(
            @Parameter(description = "ID của khách hàng") @PathVariable Long id) {
        Optional<Guest> guest = guestRepository.findById(id);
        return guest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Thêm khách hàng mới")
    @ApiResponse(responseCode = "201", description = "Tạo thành công")
    @PostMapping
    public ResponseEntity<Guest> createGuest(@RequestBody Guest guest) {
        Guest savedGuest = guestRepository.save(guest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGuest);
    }

    @Operation(summary = "Cập nhật thông tin khách hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(
            @Parameter(description = "ID của khách hàng") @PathVariable Long id,
            @RequestBody Guest guestDetails) {
        Optional<Guest> optionalGuest = guestRepository.findById(id);
        if (optionalGuest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Guest guest = optionalGuest.get();
        guest.setFullName(guestDetails.getFullName());
        guest.setEmail(guestDetails.getEmail());
        guest.setPhone(guestDetails.getPhone());
        guest.setAddress(guestDetails.getAddress());
        guest.setIdDocument(guestDetails.getIdDocument());
        guest.setLoyaltyPoints(guestDetails.getLoyaltyPoints());
        Guest updatedGuest = guestRepository.save(guest);
        return ResponseEntity.ok(updatedGuest);
    }

    @Operation(summary = "Xóa khách hàng")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khách hàng")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(
            @Parameter(description = "ID của khách hàng") @PathVariable Long id) {
        if (!guestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        guestRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Tìm kiếm khách hàng theo tên hoặc email")
    @GetMapping("/search")
    public ResponseEntity<List<Guest>> searchGuests(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam String q) {
        List<Guest> guests = guestRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q);
        return ResponseEntity.ok(guests);
    }
}
