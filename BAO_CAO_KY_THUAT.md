# BÁO CÁO KỸ THUẬT
# HỆ THỐNG QUẢN LÝ KHÁCH SẠN (HOTEL MANAGER SYSTEM)

---

## MỤC LỤC

1. [Giới thiệu đề tài](#1-giới-thiệu-đề-tài)
2. [Phân tích yêu cầu & chức năng](#2-phân-tích-yêu-cầu--chức-năng)
3. [Thiết kế Database (ORM-based)](#3-thiết-kế-database-orm-based)
4. [Thiết kế RESTful APIs](#4-thiết-kế-restful-apis)
5. [Đặc tả Swagger (OpenAPI)](#5-đặc-tả-swagger-openapi)

---

## 1. GIỚI THIỆU ĐỀ TÀI

### 1.1 Mô tả bài toán

Hệ thống Quản lý Khách sạn (Hotel Manager System) là một ứng dụng web được thiết kế để hỗ trợ các hoạt động vận hành khách sạn, bao gồm quản lý khách hàng, phòng, đặt phòng, dịch vụ, hóa đơn và thanh toán.

### 1.2 Phạm vi hệ thống

| Phạm vi | Mô tả |
|---------|-------|
| **Quản lý khách hàng** | CRUD thông tin khách, điểm tích lũy |
| **Quản lý phòng** | Quản lý loại phòng, trạng thái phòng |
| **Quản lý đặt phòng** | Đặt phòng, check-in/out, hủy đặt phòng |
| **Quản lý dịch vụ** | Danh mục dịch vụ, yêu cầu dịch vụ |
| **Quản lý tài chính** | Hóa đơn, thanh toán |

### 1.3 Công nghệ sử dụng

- **Backend**: Java Spring Boot 3.x
- **ORM**: JPA/Hibernate
- **Database**: MySQL / H2
- **API Documentation**: OpenAPI 3.0 (Swagger)
- **Frontend**: Thymeleaf

---

## 2. PHÂN TÍCH YÊU CẦU & CHỨC NĂNG

### 2.1 Xác định các Actor

```
┌─────────────────────────────────────────────────────────┐
│                    ACTORS CỦA HỆ THỐNG                   │
├─────────────────┬───────────────────────────────────────┤
│    Actor        │             Vai trò                    │
├─────────────────┼───────────────────────────────────────┤
│ Lễ tân          │ Quản lý đặt phòng, check-in/out       │
│ Quản lý         │ Quản lý phòng, dịch vụ, báo cáo       │
│ Kế toán         │ Quản lý hóa đơn, thanh toán           │
│ Khách hàng      │ Đặt phòng online (nếu có)             │
│ Hệ thống        │ Tự động tính toán, gửi thông báo      │
└─────────────────┴───────────────────────────────────────┘
```

### 2.2 Phân rã chức năng (Functional Decomposition)

```
HOTEL MANAGER SYSTEM
├── 1. QUẢN LÝ KHÁCH HÀNG (Guest Management)
│   ├── 1.1 Thêm khách hàng mới
│   ├── 1.2 Cập nhật thông tin khách
│   ├── 1.3 Xóa khách hàng
│   ├── 1.4 Tìm kiếm khách hàng
│   └── 1.5 Quản lý điểm tích lũy (Loyalty Points)
│
├── 2. QUẢN LÝ PHÒNG (Room Management)
│   ├── 2.1 Quản lý loại phòng (Room Type)
│   │   ├── Thêm/Sửa/Xóa loại phòng
│   │   └── Cập nhật giá cơ bản
│   ├── 2.2 Quản lý phòng
│   │   ├── Thêm/Sửa/Xóa phòng
│   │   └── Cập nhật trạng thái phòng
│   └── 2.3 Kiểm tra phòng trống
│
├── 3. QUẢN LÝ ĐẶT PHÒNG (Reservation Management)
│   ├── 3.1 Tạo đặt phòng mới
│   ├── 3.2 Cập nhật đặt phòng
│   ├── 3.3 Hủy đặt phòng
│   ├── 3.4 Check-in / Check-out
│   └── 3.5 Xem lịch sử đặt phòng
│
├── 4. QUẢN LÝ DỊCH VỤ (Service Management)
│   ├── 4.1 Quản lý danh mục dịch vụ
│   ├── 4.2 Yêu cầu dịch vụ (Service Request)
│   └── 4.3 Theo dõi trạng thái dịch vụ
│
├── 5. QUẢN LÝ HÓA ĐƠN (Invoice Management)
│   ├── 5.1 Tạo hóa đơn
│   ├── 5.2 Tính tổng tiền (phòng + dịch vụ + thuế)
│   └── 5.3 Xem chi tiết hóa đơn
│
└── 6. QUẢN LÝ THANH TOÁN (Payment Management)
    ├── 6.1 Ghi nhận thanh toán
    ├── 6.2 Xem lịch sử thanh toán
    └── 6.3 Báo cáo doanh thu
```

### 2.3 Chi tiết từng chức năng

#### 2.3.1 Quản lý Khách hàng (Guest Management)

| Chức năng | Mô tả | Đầu vào | Đầu ra |
|-----------|-------|---------|--------|
| **Thêm khách** | Đăng ký khách hàng mới | Họ tên, email, SĐT, CMND/CCCD | Guest ID, thông báo thành công |
| **Cập nhật khách** | Sửa thông tin khách | Guest ID, thông tin mới | Thông báo cập nhật |
| **Xóa khách** | Xóa khách khỏi hệ thống | Guest ID | Thông báo xóa |
| **Tìm kiếm** | Tìm khách theo tiêu chí | Từ khóa (tên, email, SĐT) | Danh sách khách phù hợp |
| **Quản lý điểm** | Cộng/trừ điểm loyalty | Guest ID, số điểm | Điểm tích lũy mới |

#### 2.3.2 Quản lý Phòng (Room Management)

| Chức năng | Mô tả | Đầu vào | Đầu ra |
|-----------|-------|---------|--------|
| **Thêm loại phòng** | Tạo loại phòng mới | Tên loại, sức chứa, giá | RoomType ID |
| **Thêm phòng** | Tạo phòng mới | Số phòng, loại phòng | Room ID |
| **Cập nhật trạng thái** | Đổi trạng thái phòng | Room ID, trạng thái mới | Thông báo cập nhật |
| **Kiểm tra phòng trống** | Tìm phòng available | Ngày check-in/out, loại phòng | Danh sách phòng trống |

#### 2.3.3 Quản lý Đặt phòng (Reservation Management)

| Chức năng | Mô tả | Đầu vào | Đầu ra |
|-----------|-------|---------|--------|
| **Tạo đặt phòng** | Đặt phòng cho khách | Guest ID, Room ID, ngày check-in/out | Reservation ID, mã xác nhận |
| **Cập nhật** | Sửa thông tin đặt phòng | Reservation ID, thông tin mới | Thông báo cập nhật |
| **Hủy đặt phòng** | Hủy reservation | Reservation ID | Trạng thái CANCELLED |
| **Check-in** | Nhận phòng | Reservation ID | Trạng thái CHECKED_IN |
| **Check-out** | Trả phòng | Reservation ID | Trạng thái CHECKED_OUT, tạo hóa đơn |

#### 2.3.4 Quản lý Dịch vụ (Service Management)

| Chức năng | Mô tả | Đầu vào | Đầu ra |
|-----------|-------|---------|--------|
| **Thêm dịch vụ** | Tạo dịch vụ mới | Tên, mô tả, danh mục, giá | Service ID |
| **Yêu cầu dịch vụ** | Khách yêu cầu dịch vụ | Reservation ID, Service ID, số lượng | ServiceRequest ID |
| **Cập nhật trạng thái** | Theo dõi tiến độ | Request ID, trạng thái | Trạng thái mới |

### 2.4 Đề xuất Domain Objects (Entities)

Từ phân tích chức năng trên, đề xuất **8 domain objects** chính:

| # | Entity | Mô tả |
|---|--------|-------|
| 1 | **Guest** | Thông tin khách hàng |
| 2 | **RoomType** | Loại phòng (Standard, Deluxe, Suite...) |
| 3 | **Room** | Thông tin từng phòng cụ thể |
| 4 | **Reservation** | Thông tin đặt phòng |
| 5 | **Service** | Danh mục dịch vụ khách sạn |
| 6 | **ServiceRequest** | Yêu cầu dịch vụ của khách |
| 7 | **Invoice** | Hóa đơn thanh toán |
| 8 | **Payment** | Ghi nhận các khoản thanh toán |

---

## 3. THIẾT KẾ DATABASE (ORM-BASED)

### 3.1 Sơ đồ ERD (Entity Relationship Diagram)

```
                                    ┌─────────────────┐
                                    │    RoomType     │
                                    ├─────────────────┤
                                    │ type_id     PK  │
                                    │ type_name       │
                                    │ capacity        │
                                    │ base_price      │
                                    └────────┬────────┘
                                             │ 1
                                             │
                                             │ N
┌─────────────────┐                ┌─────────┴─────────┐
│     Guest       │                │       Room        │
├─────────────────┤                ├───────────────────┤
│ guest_id    PK  │                │ id            PK  │
│ full_name       │                │ room_number       │
│ email (UNIQUE)  │                │ type_id       FK  │
│ phone           │                │ status            │
│ address         │                └─────────┬─────────┘
│ id_document     │                          │ 1
│ loyalty_points  │                          │
└────────┬────────┘                          │ N
         │ 1                         ┌───────┴────────┐
         │                           │   Reservation   │
         │ N                         ├────────────────┤
         └───────────────────────────┤ reservation_id PK│
                                     │ guest_id     FK  │
                                     │ room_id      FK  │
                                     │ check_in_date    │
                                     │ check_out_date   │
                                     │ created_date     │
                                     │ confirmation_no  │
                                     │ status           │
                                     │ number_of_guests │
                                     │ total_price      │
                                     │ special_requests │
                                     └────────┬────────┘
                                              │ 1
                     ┌───────────────────────┼───────────────────────┐
                     │                       │                       │
                     │ 1                     │ N                     │
            ┌────────┴────────┐    ┌─────────┴─────────┐            │
            │    Invoice      │    │  ServiceRequest   │            │
            ├─────────────────┤    ├───────────────────┤            │
            │ invoice_id  PK  │    │ request_id    PK  │            │
            │ reservation_id FK│   │ reservation_id FK │            │
            │ total_amount    │    │ service_id    FK  │────┐       │
            │ tax             │    │ quantity          │    │       │
            │ created_date    │    │ unit_price        │    │       │
            └────────┬────────┘    │ total_price       │    │       │
                     │ 1           │ request_time      │    │       │
                     │             │ completed_time    │    │       │
                     │ N           │ status            │    │       │
            ┌────────┴────────┐    │ notes             │    │       │
            │    Payment      │    └───────────────────┘    │       │
            ├─────────────────┤                             │       │
            │ payment_id  PK  │                       ┌─────┴───────┘
            │ invoice_id  FK  │                       │    Service
            │ amount          │                       ├─────────────────┐
            │ payment_method  │                       │ service_id  PK  │
            │ payment_date    │                       │ service_name    │
            └─────────────────┘                       │ description     │
                                                      │ category        │
                                                      │ price           │
                                                      │ unit            │
                                                      │ is_available    │
                                                      └─────────────────┘
```

### 3.2 Mô tả chi tiết các Entity

#### 3.2.1 Entity: Guest (Khách hàng)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| guestId | Long | PK, AUTO_INCREMENT | Mã khách |
| fullName | String | NOT NULL | Họ và tên |
| email | String | UNIQUE | Email liên hệ |
| phone | String | | Số điện thoại |
| address | String | | Địa chỉ |
| idDocument | String | | Số CMND/CCCD |
| loyaltyPoints | int | DEFAULT 0 | Điểm tích lũy |

**ORM Code (JPA/Hibernate):**

```java
@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long guestId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;

    @Column(name = "id_document")
    private String idDocument;

    @Column(name = "loyalty_points")
    private int loyaltyPoints;
}
```

---

#### 3.2.2 Entity: RoomType (Loại phòng)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| typeId | Long | PK, AUTO_INCREMENT | Mã loại phòng |
| typeName | String | NOT NULL, UNIQUE | Tên loại phòng |
| capacity | int | | Sức chứa (số người) |
| basePrice | BigDecimal | | Giá cơ bản/đêm |

**Relationship:** One-to-Many với Room

**ORM Code:**

```java
@Entity
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column(nullable = false, unique = true)
    private String typeName;

    private int capacity;

    @Column(precision = 10, scale = 2)
    private BigDecimal basePrice;

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}
```

**Lý do thiết kế:** Tách RoomType thành entity riêng để tránh trùng lặp dữ liệu (giá, sức chứa) và dễ dàng quản lý khi thay đổi giá theo loại phòng.

---

#### 3.2.3 Entity: Room (Phòng)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| roomId | Long | PK, AUTO_INCREMENT | Mã phòng |
| roomNumber | String | NOT NULL, UNIQUE | Số phòng (101, 102...) |
| status | String | | Trạng thái (AVAILABLE, OCCUPIED, MAINTENANCE) |
| typeId | Long | FK → RoomType | Loại phòng |

**Relationships:**
- Many-to-One với RoomType
- One-to-Many với Reservation

**ORM Code:**

```java
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long roomId;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private RoomType roomType;
}
```

---

#### 3.2.4 Entity: Reservation (Đặt phòng)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| reservationId | Long | PK, AUTO_INCREMENT | Mã đặt phòng |
| createdDate | LocalDate | | Ngày tạo |
| checkInDate | LocalDate | | Ngày nhận phòng |
| checkOutDate | LocalDate | | Ngày trả phòng |
| confirmationNumber | String | | Mã xác nhận |
| status | String | | Trạng thái (PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED) |
| numberOfGuests | int | | Số lượng khách |
| totalPrice | BigDecimal | | Tổng tiền phòng |
| specialRequests | String (TEXT) | | Yêu cầu đặc biệt |
| guestId | Long | FK → Guest | Khách đặt |
| roomId | Long | FK → Room | Phòng được đặt |

**Relationships:**
- Many-to-One với Guest
- Many-to-One với Room
- One-to-One với Invoice
- One-to-Many với ServiceRequest

**ORM Code:**

```java
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "confirmation_number")
    private String confirmationNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
}
```

**Lý do thiết kế:** Lưu totalPrice tại thời điểm đặt phòng để không bị ảnh hưởng khi giá phòng thay đổi sau này.

---

#### 3.2.5 Entity: Service (Dịch vụ)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| serviceId | Long | PK, AUTO_INCREMENT | Mã dịch vụ |
| serviceName | String | NOT NULL | Tên dịch vụ |
| description | String (TEXT) | | Mô tả chi tiết |
| category | String | | Danh mục (SPA, LAUNDRY, RESTAURANT, TRANSPORT) |
| price | BigDecimal | | Giá dịch vụ |
| unit | String | | Đơn vị (per hour, per item, per person) |
| isAvailable | boolean | DEFAULT true | Còn cung cấp hay không |

**ORM Code:**

```java
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "unit")
    private String unit;

    @Column(name = "is_available")
    private boolean isAvailable = true;

    @OneToMany(mappedBy = "service")
    private List<ServiceRequest> serviceRequests;
}
```

---

#### 3.2.6 Entity: ServiceRequest (Yêu cầu dịch vụ)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| requestId | Long | PK, AUTO_INCREMENT | Mã yêu cầu |
| reservationId | Long | FK, NOT NULL | Liên kết reservation |
| serviceId | Long | FK, NOT NULL | Dịch vụ yêu cầu |
| quantity | int | DEFAULT 1 | Số lượng |
| unitPrice | BigDecimal | | Đơn giá tại thời điểm yêu cầu |
| totalPrice | BigDecimal | | Thành tiền |
| requestTime | LocalDateTime | | Thời gian yêu cầu |
| completedTime | LocalDateTime | | Thời gian hoàn thành |
| status | String | | Trạng thái (PENDING, IN_PROGRESS, COMPLETED, CANCELLED) |
| notes | String (TEXT) | | Ghi chú |

**Lý do thiết kế:** Đây là bảng trung gian thể hiện quan hệ Many-to-Many giữa Reservation và Service, nhưng có thêm các thuộc tính riêng (quantity, status) nên tách thành entity riêng.

---

#### 3.2.7 Entity: Invoice (Hóa đơn)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| invoiceId | Long | PK, AUTO_INCREMENT | Mã hóa đơn |
| reservationId | Long | FK, UNIQUE | Liên kết reservation (1-1) |
| totalAmount | BigDecimal | NOT NULL | Tổng tiền (phòng + dịch vụ) |
| tax | BigDecimal | | Thuế VAT |
| createdDate | LocalDate | | Ngày lập hóa đơn |

**Relationships:**
- One-to-One với Reservation
- One-to-Many với Payment

**Lý do thiết kế:** Mỗi reservation chỉ có 1 hóa đơn tổng, nhưng có thể có nhiều lần thanh toán (trả trước, trả sau).

---

#### 3.2.8 Entity: Payment (Thanh toán)

| Thuộc tính | Kiểu dữ liệu | Ràng buộc | Mô tả |
|------------|--------------|-----------|-------|
| paymentId | Long | PK, AUTO_INCREMENT | Mã thanh toán |
| invoiceId | Long | FK | Hóa đơn được thanh toán |
| amount | BigDecimal | NOT NULL | Số tiền thanh toán |
| paymentMethod | String | | Phương thức (CASH, CREDIT_CARD, BANK_TRANSFER) |
| paymentDate | LocalDate | | Ngày thanh toán |

### 3.3 Tổng hợp quan hệ giữa các Entity

| Entity A | Relationship | Entity B | Mô tả |
|----------|--------------|----------|-------|
| RoomType | 1 : N | Room | Một loại phòng có nhiều phòng |
| Guest | 1 : N | Reservation | Một khách có nhiều lần đặt phòng |
| Room | 1 : N | Reservation | Một phòng có nhiều lần được đặt |
| Reservation | 1 : 1 | Invoice | Mỗi đặt phòng có 1 hóa đơn |
| Reservation | 1 : N | ServiceRequest | Một đặt phòng có nhiều yêu cầu dịch vụ |
| Service | 1 : N | ServiceRequest | Một dịch vụ được yêu cầu nhiều lần |
| Invoice | 1 : N | Payment | Một hóa đơn có thể thanh toán nhiều lần |

---

## 4. THIẾT KẾ RESTful APIs

### 4.1 Quy ước chung

- **Base URL**: `/api/v1`
- **Format**: JSON
- **Authentication**: Bearer Token (nếu có)
- **Naming**: Sử dụng danh từ số nhiều (guests, rooms, reservations)

### 4.2 HTTP Status Codes

| Code | Ý nghĩa |
|------|---------|
| 200 | OK - Thành công |
| 201 | Created - Tạo mới thành công |
| 204 | No Content - Xóa thành công |
| 400 | Bad Request - Dữ liệu không hợp lệ |
| 404 | Not Found - Không tìm thấy |
| 500 | Internal Server Error |

### 4.3 Guest APIs

| Method | Endpoint | Mô tả | Request Body | Response |
|--------|----------|-------|--------------|----------|
| GET | `/api/v1/guests` | Lấy danh sách khách | - | Array of Guest |
| GET | `/api/v1/guests/{id}` | Lấy thông tin 1 khách | - | Guest object |
| POST | `/api/v1/guests` | Thêm khách mới | Guest object | Guest object (201) |
| PUT | `/api/v1/guests/{id}` | Cập nhật thông tin khách | Guest object | Guest object |
| DELETE | `/api/v1/guests/{id}` | Xóa khách | - | 204 No Content |
| GET | `/api/v1/guests/search?q={keyword}` | Tìm kiếm khách | - | Array of Guest |

**Request Body mẫu (POST/PUT):**
```json
{
  "fullName": "Nguyen Van A",
  "email": "nguyenvana@email.com",
  "phone": "0901234567",
  "address": "123 Le Loi, Q1, TP.HCM",
  "idDocument": "079123456789"
}
```

**Response mẫu (GET /api/v1/guests/{id}):**
```json
{
  "guestId": 1,
  "fullName": "Nguyen Van A",
  "email": "nguyenvana@email.com",
  "phone": "0901234567",
  "address": "123 Le Loi, Q1, TP.HCM",
  "idDocument": "079123456789",
  "loyaltyPoints": 150
}
```

---

### 4.4 Room APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/v1/rooms` | Lấy danh sách phòng |
| GET | `/api/v1/rooms/{id}` | Lấy thông tin phòng |
| GET | `/api/v1/rooms/available` | Lấy danh sách phòng trống |
| POST | `/api/v1/rooms` | Thêm phòng mới |
| PUT | `/api/v1/rooms/{id}` | Cập nhật phòng |
| PATCH | `/api/v1/rooms/{id}/status` | Đổi trạng thái phòng |

**Request Body mẫu (POST):**
```json
{
  "roomNumber": "101",
  "status": "AVAILABLE",
  "roomType": { "typeId": 1 }
}
```

**Response mẫu (GET /api/v1/rooms/{id}):**
```json
{
  "roomId": 1,
  "roomNumber": "101",
  "status": "AVAILABLE",
  "roomType": {
    "typeId": 1,
    "typeName": "Deluxe",
    "capacity": 2,
    "basePrice": 1500000.00
  }
}
```

---

### 4.5 Room Type APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/v1/room-types` | Danh sách loại phòng |
| GET | `/api/v1/room-types/{id}` | Chi tiết loại phòng |
| POST | `/api/v1/room-types` | Thêm loại phòng |
| PUT | `/api/v1/room-types/{id}` | Cập nhật loại phòng |
| DELETE | `/api/v1/room-types/{id}` | Xóa loại phòng |

---

### 4.6 Reservation APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/v1/reservations` | Danh sách đặt phòng |
| GET | `/api/v1/reservations/{id}` | Chi tiết đặt phòng |
| POST | `/api/v1/reservations` | Tạo đặt phòng mới |
| PUT | `/api/v1/reservations/{id}` | Cập nhật đặt phòng |
| PATCH | `/api/v1/reservations/{id}/check-in` | Check-in |
| PATCH | `/api/v1/reservations/{id}/check-out` | Check-out |
| PATCH | `/api/v1/reservations/{id}/cancel` | Hủy đặt phòng |

**Request Body mẫu (POST):**
```json
{
  "guest": { "guestId": 1 },
  "room": { "roomId": 5 },
  "checkInDate": "2026-02-01",
  "checkOutDate": "2026-02-03",
  "numberOfGuests": 2,
  "specialRequests": "Phòng hướng biển, tầng cao"
}
```

**Response mẫu (GET /api/v1/reservations/{id}):**
```json
{
  "reservationId": 1,
  "createdDate": "2026-01-28",
  "checkInDate": "2026-02-01",
  "checkOutDate": "2026-02-03",
  "confirmationNumber": "RES-20260128-001",
  "status": "CONFIRMED",
  "numberOfGuests": 2,
  "totalPrice": 3000000.00,
  "specialRequests": "Phòng hướng biển, tầng cao",
  "guest": {
    "guestId": 1,
    "fullName": "Nguyen Van A"
  },
  "room": {
    "roomId": 5,
    "roomNumber": "501",
    "roomType": { "typeName": "Deluxe" }
  }
}
```

---

### 4.7 Service APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/v1/services` | Danh sách dịch vụ |
| GET | `/api/v1/services?category={cat}` | Lọc theo danh mục |
| GET | `/api/v1/services/{id}` | Chi tiết dịch vụ |
| POST | `/api/v1/services` | Thêm dịch vụ |
| PUT | `/api/v1/services/{id}` | Cập nhật dịch vụ |
| DELETE | `/api/v1/services/{id}` | Xóa dịch vụ |

---

### 4.8 Service Request APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/v1/service-requests` | Danh sách yêu cầu |
| GET | `/api/v1/reservations/{id}/service-requests` | Yêu cầu theo reservation |
| POST | `/api/v1/service-requests` | Tạo yêu cầu mới |
| PATCH | `/api/v1/service-requests/{id}/status` | Cập nhật trạng thái |
| DELETE | `/api/v1/service-requests/{id}` | Hủy yêu cầu |

**Request Body mẫu (POST):**
```json
{
  "reservation": { "reservationId": 1 },
  "service": { "serviceId": 3 },
  "quantity": 2,
  "notes": "Giao buổi sáng"
}
```

---

### 4.9 Invoice APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/v1/invoices` | Danh sách hóa đơn |
| GET | `/api/v1/invoices/{id}` | Chi tiết hóa đơn |
| POST | `/api/v1/invoices` | Tạo hóa đơn |
| GET | `/api/v1/reservations/{id}/invoice` | Hóa đơn theo reservation |

---

### 4.10 Payment APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/v1/payments` | Danh sách thanh toán |
| POST | `/api/v1/payments` | Ghi nhận thanh toán |
| GET | `/api/v1/invoices/{id}/payments` | Thanh toán theo hóa đơn |

**Request Body mẫu (POST):**
```json
{
  "invoice": { "invoiceId": 1 },
  "amount": 2000000.00,
  "paymentMethod": "CREDIT_CARD",
  "paymentDate": "2026-02-03"
}
```

---

## 5. ĐẶC TẢ SWAGGER (OpenAPI)

### 5.1 Cấu hình Swagger trong Spring Boot

**pom.xml:**
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

**OpenApiConfig.java:**
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI hotelManagerOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Hotel Manager API")
                .description("RESTful API cho hệ thống quản lý khách sạn")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Developer Team")
                    .email("dev@hotel.com")));
    }
}
```

**Truy cập:**
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### 5.2 OpenAPI Specification (YAML)

```yaml
openapi: 3.0.3
info:
  title: Hotel Manager API
  description: RESTful API cho hệ thống quản lý khách sạn
  version: 1.0.0
  contact:
    name: Developer Team
    email: dev@hotel.com

servers:
  - url: http://localhost:8080/api/v1
    description: Development server

tags:
  - name: Guests
    description: Quản lý khách hàng
  - name: Rooms
    description: Quản lý phòng
  - name: Reservations
    description: Quản lý đặt phòng
  - name: Services
    description: Quản lý dịch vụ
  - name: Invoices
    description: Quản lý hóa đơn
  - name: Payments
    description: Quản lý thanh toán

paths:
  # ==================== GUEST APIs ====================
  /guests:
    get:
      tags:
        - Guests
      summary: Lấy danh sách tất cả khách hàng
      operationId: getAllGuests
      parameters:
        - name: page
          in: query
          description: Số trang (bắt đầu từ 0)
          schema:
            type: integer
            default: 0
        - name: size
          in: query
          description: Số lượng trên mỗi trang
          schema:
            type: integer
            default: 10
      responses:
        '200':
          description: Thành công
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Guest'
        '500':
          description: Lỗi server

    post:
      tags:
        - Guests
      summary: Thêm khách hàng mới
      operationId: createGuest
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/GuestRequest'
      responses:
        '201':
          description: Tạo thành công
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Guest'
        '400':
          description: Dữ liệu không hợp lệ

  /guests/{id}:
    get:
      tags:
        - Guests
      summary: Lấy thông tin một khách hàng
      operationId: getGuestById
      parameters:
        - name: id
          in: path
          required: true
          description: ID của khách hàng
          schema:
            type: integer
            format: int64
      responses:
        '200':
          description: Thành công
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Guest'
        '404':
          description: Không tìm thấy khách hàng

    put:
      tags:
        - Guests
      summary: Cập nhật thông tin khách hàng
      operationId: updateGuest
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: integer
            format: int64
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/GuestRequest'
      responses:
        '200':
          description: Cập nhật thành công
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Guest'
        '404':
          description: Không tìm thấy khách hàng

    delete:
      tags:
        - Guests
      summary: Xóa khách hàng
      operationId: deleteGuest
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: integer
            format: int64
      responses:
        '204':
          description: Xóa thành công
        '404':
          description: Không tìm thấy khách hàng

  # ==================== RESERVATION APIs ====================
  /reservations:
    get:
      tags:
        - Reservations
      summary: Lấy danh sách đặt phòng
      operationId: getAllReservations
      parameters:
        - name: status
          in: query
          description: Lọc theo trạng thái
          schema:
            type: string
            enum: [PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED]
        - name: fromDate
          in: query
          description: Từ ngày (check-in)
          schema:
            type: string
            format: date
        - name: toDate
          in: query
          description: Đến ngày (check-in)
          schema:
            type: string
            format: date
      responses:
        '200':
          description: Thành công
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Reservation'

    post:
      tags:
        - Reservations
      summary: Tạo đặt phòng mới
      operationId: createReservation
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ReservationRequest'
      responses:
        '201':
          description: Tạo thành công
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Reservation'
        '400':
          description: Dữ liệu không hợp lệ hoặc phòng đã được đặt

  /reservations/{id}/check-in:
    patch:
      tags:
        - Reservations
      summary: Check-in cho khách
      operationId: checkIn
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: integer
            format: int64
      responses:
        '200':
          description: Check-in thành công
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Reservation'
        '400':
          description: Không thể check-in (sai trạng thái)
        '404':
          description: Không tìm thấy đặt phòng

  /reservations/{id}/check-out:
    patch:
      tags:
        - Reservations
      summary: Check-out cho khách
      operationId: checkOut
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: integer
            format: int64
      responses:
        '200':
          description: Check-out thành công
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Reservation'

components:
  schemas:
    # ===== Guest =====
    Guest:
      type: object
      properties:
        guestId:
          type: integer
          format: int64
          description: ID khách hàng
          example: 1
        fullName:
          type: string
          description: Họ và tên
          example: "Nguyen Van A"
        email:
          type: string
          format: email
          description: Email
          example: "nguyenvana@email.com"
        phone:
          type: string
          description: Số điện thoại
          example: "0901234567"
        address:
          type: string
          description: Địa chỉ
          example: "123 Le Loi, Q1, TP.HCM"
        idDocument:
          type: string
          description: Số CMND/CCCD
          example: "079123456789"
        loyaltyPoints:
          type: integer
          description: Điểm tích lũy
          example: 150

    GuestRequest:
      type: object
      required:
        - fullName
      properties:
        fullName:
          type: string
          minLength: 2
          maxLength: 100
        email:
          type: string
          format: email
        phone:
          type: string
          pattern: '^\d{10,11}$'
        address:
          type: string
        idDocument:
          type: string

    # ===== Room =====
    Room:
      type: object
      properties:
        roomId:
          type: integer
          format: int64
        roomNumber:
          type: string
          example: "101"
        status:
          type: string
          enum: [AVAILABLE, OCCUPIED, MAINTENANCE]
        roomType:
          $ref: '#/components/schemas/RoomType'

    RoomType:
      type: object
      properties:
        typeId:
          type: integer
          format: int64
        typeName:
          type: string
          example: "Deluxe"
        capacity:
          type: integer
          example: 2
        basePrice:
          type: number
          format: decimal
          example: 1500000.00

    # ===== Reservation =====
    Reservation:
      type: object
      properties:
        reservationId:
          type: integer
          format: int64
        createdDate:
          type: string
          format: date
        checkInDate:
          type: string
          format: date
        checkOutDate:
          type: string
          format: date
        confirmationNumber:
          type: string
          example: "RES-20260128-001"
        status:
          type: string
          enum: [PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED]
        numberOfGuests:
          type: integer
        totalPrice:
          type: number
          format: decimal
        specialRequests:
          type: string
        guest:
          $ref: '#/components/schemas/Guest'
        room:
          $ref: '#/components/schemas/Room'

    ReservationRequest:
      type: object
      required:
        - guestId
        - roomId
        - checkInDate
        - checkOutDate
      properties:
        guestId:
          type: integer
          format: int64
        roomId:
          type: integer
          format: int64
        checkInDate:
          type: string
          format: date
        checkOutDate:
          type: string
          format: date
        numberOfGuests:
          type: integer
          default: 1
        specialRequests:
          type: string

    # ===== Service =====
    Service:
      type: object
      properties:
        serviceId:
          type: integer
          format: int64
        serviceName:
          type: string
        description:
          type: string
        category:
          type: string
          enum: [SPA, LAUNDRY, RESTAURANT, TRANSPORT, ENTERTAINMENT, OTHER]
        price:
          type: number
          format: decimal
        unit:
          type: string
        isAvailable:
          type: boolean

    # ===== Invoice =====
    Invoice:
      type: object
      properties:
        invoiceId:
          type: integer
          format: int64
        totalAmount:
          type: number
          format: decimal
        tax:
          type: number
          format: decimal
        createdDate:
          type: string
          format: date
        reservation:
          $ref: '#/components/schemas/Reservation'

    # ===== Payment =====
    Payment:
      type: object
      properties:
        paymentId:
          type: integer
          format: int64
        amount:
          type: number
          format: decimal
        paymentMethod:
          type: string
          enum: [CASH, CREDIT_CARD, BANK_TRANSFER, MOMO, VNPAY]
        paymentDate:
          type: string
          format: date
```

---

## PHỤ LỤC

### A. Các giá trị ENUM đề xuất

| Enum | Giá trị |
|------|---------|
| **RoomStatus** | AVAILABLE, OCCUPIED, MAINTENANCE, CLEANING |
| **ReservationStatus** | PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED |
| **ServiceCategory** | SPA, LAUNDRY, RESTAURANT, TRANSPORT, ENTERTAINMENT, OTHER |
| **ServiceRequestStatus** | PENDING, IN_PROGRESS, COMPLETED, CANCELLED |
| **PaymentMethod** | CASH, CREDIT_CARD, BANK_TRANSFER, MOMO, VNPAY |

### B. Tài liệu tham khảo

- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/reference/)
- [OpenAPI 3.0 Specification](https://swagger.io/specification/)
- [RESTful API Design Best Practices](https://restfulapi.net/)

---

**Ngày lập:** 29/01/2026  
**Phiên bản:** 1.0
