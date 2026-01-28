# 📋 TÀI LIỆU THIẾT KẾ API - HOTEL MANAGER SYSTEM

## 📑 MỤC LỤC
1. [Phân Rã Chức Năng](#1-phân-rã-chức-năng)
2. [Database Schema (ORM)](#2-database-schema-orm)
3. [Thiết Kế RESTful APIs](#3-thiết-kế-restful-apis)
4. [Swagger Integration](#4-swagger-integration)

---

## 1. PHÂN RÃ CHỨC NĂNG

### 1.1 Sơ đồ phân rã chức năng (Function Decomposition)

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

---

## 2. DATABASE SCHEMA (ORM)

### 2.1 Entity Relationship Diagram

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│  RoomType   │1     N│    Room     │1     N│ Reservation │
├─────────────┤───────├─────────────┤───────├─────────────┤
│ typeId (PK) │       │ roomId (PK) │       │reservationId│
│ typeName    │       │ roomNumber  │       │ checkInDate │
│ capacity    │       │ status      │       │ checkOutDate│
│ basePrice   │       │ typeId (FK) │       │ status      │
└─────────────┘       └─────────────┘       │ guestId(FK) │
                                            │ roomId (FK) │
┌─────────────┐                             └──────┬──────┘
│   Guest     │1                              1    │
├─────────────┤──────────────────────────────N─────┘
│ guestId(PK) │
│ fullName    │     ┌─────────────┐     ┌─────────────┐
│ email       │     │   Invoice   │1   N│   Payment   │
│ phone       │     ├─────────────┤─────├─────────────┤
│loyaltyPoints│     │invoiceId(PK)│     │paymentId(PK)│
└─────────────┘     │totalAmount  │     │ amount      │
                    │ tax         │     │paymentMethod│
                    │reservation  │     │invoiceId(FK)│
                    └─────────────┘     └─────────────┘

┌─────────────┐       ┌───────────────┐
│   Service   │1     N│ServiceRequest │
├─────────────┤───────├───────────────┤
│serviceId(PK)│       │ requestId(PK) │
│ serviceName │       │reservationId  │
│ category    │       │ serviceId(FK) │
│ price       │       │ quantity      │
│ isAvailable │       │ status        │
└─────────────┘       └───────────────┘
```

### 2.2 Các Entity đã triển khai

| Entity | Table Name | Primary Key | Relationships |
|--------|------------|-------------|---------------|
| Guest | guests | guest_id | 1:N → Reservation |
| RoomType | room_types | type_id | 1:N → Room |
| Room | rooms | id | N:1 → RoomType, 1:N → Reservation |
| Reservation | reservations | reservation_id | N:1 → Guest, N:1 → Room, 1:1 → Invoice |
| Service | services | service_id | 1:N → ServiceRequest |
| ServiceRequest | service_requests | request_id | N:1 → Reservation, N:1 → Service |
| Invoice | invoice | invoice_id | 1:1 → Reservation, 1:N → Payment |
| Payment | payments | payment_id | N:1 → Invoice |

---

## 3. THIẾT KẾ RESTful APIs

### 3.1 Guest APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/guests` | Lấy danh sách khách hàng |
| GET | `/api/guests/{id}` | Lấy thông tin 1 khách |
| POST | `/api/guests` | Thêm khách mới |
| PUT | `/api/guests/{id}` | Cập nhật khách |
| DELETE | `/api/guests/{id}` | Xóa khách |
| GET | `/api/guests/search?q={keyword}` | Tìm kiếm |

### 3.2 Room APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/rooms` | Lấy danh sách phòng |
| GET | `/api/rooms/{id}` | Lấy thông tin phòng |
| GET | `/api/rooms/available` | Phòng trống |
| POST | `/api/rooms` | Thêm phòng mới |
| PUT | `/api/rooms/{id}` | Cập nhật phòng |
| PATCH | `/api/rooms/{id}/status` | Đổi trạng thái |

### 3.3 Room Type APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/room-types` | Danh sách loại phòng |
| POST | `/api/room-types` | Thêm loại phòng |
| PUT | `/api/room-types/{id}` | Cập nhật |

### 3.4 Reservation APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/reservations` | Danh sách đặt phòng |
| GET | `/api/reservations/{id}` | Chi tiết đặt phòng |
| POST | `/api/reservations` | Tạo đặt phòng |
| PUT | `/api/reservations/{id}` | Cập nhật |
| PATCH | `/api/reservations/{id}/check-in` | Check-in |
| PATCH | `/api/reservations/{id}/check-out` | Check-out |
| PATCH | `/api/reservations/{id}/cancel` | Hủy đặt phòng |

### 3.5 Service APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/services` | Danh sách dịch vụ |
| GET | `/api/services?category={cat}` | Lọc theo danh mục |
| POST | `/api/services` | Thêm dịch vụ |
| PUT | `/api/services/{id}` | Cập nhật |

### 3.6 Service Request APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/service-requests` | Danh sách yêu cầu |
| POST | `/api/service-requests` | Tạo yêu cầu mới |
| PATCH | `/api/service-requests/{id}/status` | Cập nhật trạng thái |

### 3.7 Invoice APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/invoices` | Danh sách hóa đơn |
| GET | `/api/invoices/{id}` | Chi tiết hóa đơn |
| POST | `/api/invoices` | Tạo hóa đơn |
| GET | `/api/reservations/{id}/invoice` | Hóa đơn theo reservation |

### 3.8 Payment APIs

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/payments` | Danh sách thanh toán |
| POST | `/api/payments` | Ghi nhận thanh toán |
| GET | `/api/invoices/{id}/payments` | Thanh toán theo hóa đơn |

---

## 4. SWAGGER INTEGRATION

### 4.1 Thêm dependency vào pom.xml

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 4.2 Cấu hình Swagger

```java
// config/OpenApiConfig.java
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
                    .name("Developer")
                    .email("dev@hotel.com")));
    }
}
```

### 4.3 Truy cập Swagger UI

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

---

## 📅 Cập nhật: 28/01/2026
