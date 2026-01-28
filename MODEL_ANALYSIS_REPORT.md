# 📊 BÁO CÁO PHÂN TÍCH MODEL - HOTEL MANAGER SYSTEM

## 🏗️ TỔNG QUAN CẤU TRÚC

```
com.duong.salesmanagement/
├── controller/    ← Xử lý request HTTP
├── model/         ← Định nghĩa cấu trúc dữ liệu (Entity)
├── repository/    ← Truy vấn database
└── service/       ← Logic nghiệp vụ
```

---

## 📁 DANH SÁCH CÁC MODEL (8 files)

| # | Model | Trạng thái | Đánh giá |
|---|-------|------------|----------|
| 1 | Guest.java | ✅ Hoàn chỉnh | Tốt |
| 2 | Room.java | ⚠️ Cần cải thiện | Trung bình |
| 3 | Reservation.java | ⚠️ Cần cải thiện | Trung bình |
| 4 | Invoice.java | ✅ Hoàn chỉnh | Tốt |
| 5 | Payment.java | ✅ Hoàn chỉnh | Tốt |
| 6 | Service.java | ⚠️ Thiếu quan hệ | Trung bình |
| 7 | RoomType.java | ❌ File rỗng | Chưa triển khai |
| 8 | ServiceRequest.java | ❌ File rỗng | Chưa triển khai |

---

## 🔍 PHÂN TÍCH CHI TIẾT TỪNG MODEL

### 1️⃣ Guest.java ✅ TỐT

**Cấu trúc:**
```java
@Entity @Table(name = "guests")
- guestId (Long, PK, AUTO_INCREMENT)
- fullName (String, NOT NULL)
- email (String, UNIQUE)
- phone (String)
- address (String)
- idDocument (String)
- loyaltyPoints (int)
```

**Điểm mạnh:**
- ✅ Đầy đủ annotation JPA (@Entity, @Table, @Column)
- ✅ Có constructor mặc định cho JPA
- ✅ Có constructor tiện ích
- ✅ Email được đánh dấu UNIQUE
- ✅ Có hệ thống loyalty points

**Điểm cần cải thiện:**
- ⚠️ Thiếu validation (@NotBlank, @Email, @Pattern cho phone)
- ⚠️ Thiếu quan hệ @OneToMany với Reservation (để biết khách có bao nhiêu lần đặt)

---

### 2️⃣ Room.java ⚠️ CẦN CẢI THIỆN

**Cấu trúc:**
```java
@Entity @Table(name = "rooms")
- roomId (Long, PK) → Column name = "id" (không nhất quán)
- roomNumber (String, NOT NULL, UNIQUE)
- roomType (String) → Nên là quan hệ với RoomType
- status (String) → Nên dùng ENUM
- type (String) → Trùng lặp với roomType?
```

**Vấn đề nghiêm trọng:**
- ❌ **Trùng lặp dữ liệu:** Có 2 field `roomType` và `type` → confusing
- ❌ **Thiếu quan hệ:** `roomType` nên là @ManyToOne với RoomType entity
- ❌ **Thiếu thông tin:** Không có giá phòng (price), số giường, diện tích
- ❌ **Status dùng String:** Nên dùng ENUM (AVAILABLE, OCCUPIED, MAINTENANCE, CLEANING)

**Khuyến nghị:**
```java
// Nên thêm:
@ManyToOne
@JoinColumn(name = "room_type_id")
private RoomType roomType;

@Enumerated(EnumType.STRING)
private RoomStatus status;

private BigDecimal pricePerNight;
private int capacity;
private int floor;
```

---

### 3️⃣ Reservation.java ⚠️ CẦN CẢI THIỆN

**Cấu trúc:**
```java
@Entity @Table(name = "reservations")
- reservation_id (int) → Nên dùng Long
- created_date (LocalDate)
- check_in_date (LocalDate)
- check_out_date (LocalDate)
- confirmation_number (String)
- status (String) → Nên dùng ENUM
- guest (@ManyToOne)
- room (@ManyToOne)
```

**Vấn đề:**
- ❌ **Naming convention sai:** Dùng `snake_case` thay vì `camelCase`
  - `reservation_id` → nên là `reservationId`
  - `check_in_date` → nên là `checkInDate`
- ❌ **Kiểu dữ liệu sai:** `reservation_id` dùng `int` thay vì `Long`
- ❌ **Thiếu thông tin:** Không có tổng tiền (totalPrice), số khách (numberOfGuests)
- ❌ **Status dùng String:** Nên dùng ENUM

**Khuyến nghị:**
```java
// Naming convention đúng:
private Long reservationId;
private LocalDate checkInDate;
private LocalDate checkOutDate;

// Thêm fields:
@Enumerated(EnumType.STRING)
private ReservationStatus status; // PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED

private int numberOfGuests;
private BigDecimal totalPrice;
private String specialRequests; // Yêu cầu đặc biệt
```

---

### 4️⃣ Invoice.java ✅ TỐT

**Cấu trúc:**
```java
@Entity @Table(name = "invoice")
- invoiceId (Long, PK)
- totalAmount (double, NOT NULL)
- tax (double)
- createdDate (LocalDate)
- reservation (@OneToOne)
- payments (@OneToMany)
```

**Điểm mạnh:**
- ✅ Quan hệ đúng: 1 Reservation → 1 Invoice
- ✅ Có cascade cho payments
- ✅ Có constructor tiện ích

**Điểm cần cải thiện:**
- ⚠️ `totalAmount` nên dùng `BigDecimal` thay vì `double` (chính xác hơn cho tiền)
- ⚠️ Thiếu invoiceNumber (mã hóa đơn)
- ⚠️ Thiếu status (UNPAID, PAID, OVERDUE)

---

### 5️⃣ Payment.java ✅ TỐT

**Cấu trúc:**
```java
@Entity @Table(name = "payments")
- paymentId (Long, PK)
- amount (double, NOT NULL)
- paymentMethod (String)
- paymentDate (LocalDate)
- invoice (@ManyToOne)
```

**Điểm mạnh:**
- ✅ Quan hệ đúng: Nhiều Payment → 1 Invoice
- ✅ Cấu trúc rõ ràng

**Điểm cần cải thiện:**
- ⚠️ `amount` nên dùng `BigDecimal`
- ⚠️ `paymentMethod` nên dùng ENUM (CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER)
- ⚠️ Thiếu transactionId (mã giao dịch)

---

### 6️⃣ Service.java ⚠️ CẦN CẢI THIỆN

**Cấu trúc:**
```java
@Entity @Table(name = "services")
- serviceId (Long, PK)
- serviceName (String, NOT NULL)
- price (double)
```

**Vấn đề:**
- ❌ **Thiếu mô tả:** Không có field `description`
- ❌ **Thiếu phân loại:** Không có `category` (SPA, LAUNDRY, RESTAURANT, etc.)
- ❌ **Đứng một mình:** Không có quan hệ với ServiceRequest

---

### 7️⃣ RoomType.java ❌ FILE RỖNG

**Cần triển khai:**
```java
@Entity
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomTypeId;
    
    private String typeName; // Standard, Deluxe, Suite, Presidential
    private String description;
    private BigDecimal basePrice;
    private int maxOccupancy;
    
    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}
```

---

### 8️⃣ ServiceRequest.java ❌ FILE RỖNG

**Cần triển khai:**
```java
@Entity
@Table(name = "service_requests")
public class ServiceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;
    
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
    
    private int quantity;
    private LocalDateTime requestTime;
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    private String notes;
}
```

---

## 📐 SƠ ĐỒ ERD (Entity Relationship Diagram)

```
                                    ┌─────────────────┐
                                    │    RoomType     │
                                    ├─────────────────┤
                                    │ room_type_id PK │
                                    │ type_name       │
                                    │ description     │
                                    │ base_price      │
                                    │ max_occupancy   │
                                    └────────┬────────┘
                                             │ 1
                                             │
                                             │ N
┌─────────────────┐                ┌─────────┴─────────┐
│     Guest       │                │       Room        │
├─────────────────┤                ├───────────────────┤
│ guest_id    PK  │                │ id            PK  │
│ full_name       │                │ room_number       │
│ email           │                │ room_type     FK  │──┐
│ phone           │                │ status            │  │
│ address         │                │ floor             │  │
│ id_document     │                └─────────┬─────────┘  │
│ loyalty_points  │                          │ 1          │
└────────┬────────┘                          │            │
         │ 1                                 │ N          │
         │                          ┌────────┴────────┐   │
         │ N                        │   Reservation   │   │
         └──────────────────────────┤─────────────────┤   │
                                    │ reservation_id PK│   │
                                    │ guest_id     FK  │───┘
                                    │ room_id      FK  │
                                    │ check_in_date    │
                                    │ check_out_date   │
                                    │ created_date     │
                                    │ confirmation_no  │
                                    │ status           │
                                    └────────┬────────┘
                                             │ 1
                     ┌───────────────────────┼───────────────────────┐
                     │                       │                       │
                     │ 1                     │ N                     │
            ┌────────┴────────┐    ┌─────────┴─────────┐            │
            │    Invoice      │    │  ServiceRequest   │            │
            ├─────────────────┤    ├───────────────────┤            │
            │ invoice_id  PK  │    │ request_id    PK  │            │
            │ reservation_id FK│    │ reservation_id FK │            │
            │ total_amount    │    │ service_id    FK  │────┐       │
            │ tax             │    │ quantity          │    │       │
            │ created_date    │    │ request_time      │    │       │
            └────────┬────────┘    │ status            │    │       │
                     │ 1           └───────────────────┘    │       │
                     │                                      │       │
                     │ N                              ┌─────┴───────┴───┐
            ┌────────┴────────┐                       │    Service      │
            │    Payment      │                       ├─────────────────┤
            ├─────────────────┤                       │ service_id  PK  │
            │ payment_id  PK  │                       │ service_name    │
            │ invoice_id  FK  │                       │ price           │
            │ amount          │                       │ description     │
            │ payment_method  │                       │ category        │
            │ payment_date    │                       └─────────────────┘
            └─────────────────┘
```

---

## 🔗 SƠ ĐỒ ORM RELATIONSHIPS

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         ORM RELATIONSHIPS                                │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   Guest ─────────────< Reservation >─────────────  Room                  │
│    (1)       @OneToMany      (N)      @ManyToOne    (1)                 │
│                                                                          │
│   RoomType ─────────────< Room                                           │
│     (1)       @OneToMany     (N)                                         │
│                                                                          │
│   Reservation ─────────────  Invoice                                     │
│       (1)       @OneToOne      (1)                                       │
│                                                                          │
│   Invoice ─────────────< Payment                                         │
│     (1)      @OneToMany    (N)                                           │
│                                                                          │
│   Reservation ─────────────< ServiceRequest >───────────── Service       │
│       (1)       @OneToMany        (N)        @ManyToOne      (1)         │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘

LEGEND:
─────────────< : One-to-Many relationship
───────────── : One-to-One relationship
```

---

## 📋 TÓM TẮT CÁC VẤN ĐỀ CẦN SỬA

### 🔴 NGHIÊM TRỌNG (Cần sửa ngay)
1. **Reservation.java:** Đổi naming convention sang camelCase
2. **Reservation.java:** Đổi `int reservation_id` thành `Long reservationId`
3. **RoomType.java:** Triển khai entity
4. **ServiceRequest.java:** Triển khai entity

### 🟡 QUAN TRỌNG (Nên sửa)
1. **Room.java:** Xóa field `type` trùng lặp, tạo quan hệ với RoomType
2. **Tất cả tiền:** Đổi `double` thành `BigDecimal`
3. **Tất cả status:** Đổi `String` thành ENUM

### 🟢 CẢI THIỆN (Tùy chọn)
1. Thêm validation annotations (@NotBlank, @Email, etc.)
2. Thêm @Temporal cho LocalDate
3. Thêm @ToString, @EqualsAndHashCode (hoặc dùng Lombok)

---

## ✅ ĐỀ XUẤT TIẾP THEO

1. Sửa naming convention trong Reservation.java
2. Triển khai RoomType.java
3. Triển khai ServiceRequest.java
4. Tạo các ENUM cho status
5. Thêm validation annotations

Bạn muốn tôi thực hiện bước nào trước?
