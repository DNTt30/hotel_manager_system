package com.duong.salesmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

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
    private String status; // PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED

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

    // ===== Constructors =====
    public Reservation() {
    }

    public Reservation(LocalDate checkInDate, LocalDate checkOutDate, Guest guest, Room room) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guest = guest;
        this.room = room;
        this.createdDate = LocalDate.now();
        this.status = "PENDING";
    }

    // ===== Getter & Setter =====

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    // ===== Deprecated getters/setters for backward compatibility =====
    @Deprecated
    public int getReservation_id() {
        return reservationId != null ? reservationId.intValue() : 0;
    }

    @Deprecated
    public void setReservation_id(int reservation_id) {
        this.reservationId = (long) reservation_id;
    }

    @Deprecated
    public LocalDate getCreated_date() {
        return createdDate;
    }

    @Deprecated
    public void setCreated_date(LocalDate created_date) {
        this.createdDate = created_date;
    }

    @Deprecated
    public LocalDate getCheck_in_date() {
        return checkInDate;
    }

    @Deprecated
    public void setCheck_in_date(LocalDate check_in_date) {
        this.checkInDate = check_in_date;
    }

    @Deprecated
    public LocalDate getCheck_out_date() {
        return checkOutDate;
    }

    @Deprecated
    public void setCheck_out_date(LocalDate check_out_date) {
        this.checkOutDate = check_out_date;
    }

    @Deprecated
    public String getConfirmation_number() {
        return confirmationNumber;
    }

    @Deprecated
    public void setConfirmation_number(String confirmation_number) {
        this.confirmationNumber = confirmation_number;
    }
}
