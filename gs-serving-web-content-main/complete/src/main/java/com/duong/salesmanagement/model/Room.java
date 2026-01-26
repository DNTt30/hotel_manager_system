package com.duong.salesmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // Sửa từ "room_id" thành "id" để khớp với ảnh image_ba90ee.png
    private Long roomId;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(name = "room_type") // Ánh xạ vào cột chứa "Standard Single" trong ảnh
    private String roomType;

    @Column(name = "status")
    private String status;

    @Column(name = "type") // Ánh xạ vào cột "type" (đang NULL trong ảnh)
    private String type;

    public Room() {}

    // ===== Getter & Setter =====
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}