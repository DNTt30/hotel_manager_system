package com.duong.salesmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "tax")
    private double tax;

    @Column(name = "created_date")
    private LocalDate createdDate;

    // 1 Reservation -> 1 Invoice
    @OneToOne
    @JoinColumn(name = "reservation_id", unique = true)
    private Reservation reservation;

    // 1 Invoice -> nhiều Payment
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Payment> payments;

    // ===== Constructor =====
    public Invoice() {
    }

    public Invoice(double totalAmount, double tax, LocalDate createdDate) {
        this.totalAmount = totalAmount;
        this.tax = tax;
        this.createdDate = createdDate;
    }

    // ===== Getter & Setter =====
    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
