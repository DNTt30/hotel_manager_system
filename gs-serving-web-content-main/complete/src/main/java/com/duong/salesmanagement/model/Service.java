package com.duong.salesmanagement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

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
    private String category; // SPA, LAUNDRY, RESTAURANT, TRANSPORT, ENTERTAINMENT, OTHER

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "unit")
    private String unit; // per hour, per item, per person, etc.

    @Column(name = "is_available")
    private boolean isAvailable = true;

    @OneToMany(mappedBy = "service")
    private List<ServiceRequest> serviceRequests;

    // ===== Constructors =====
    public Service() {
    }

    public Service(String serviceName, String category, BigDecimal price) {
        this.serviceName = serviceName;
        this.category = category;
        this.price = price;
        this.isAvailable = true;
    }

    // ===== Getter & Setter =====

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<ServiceRequest> getServiceRequests() {
        return serviceRequests;
    }

    public void setServiceRequests(List<ServiceRequest> serviceRequests) {
        this.serviceRequests = serviceRequests;
    }
}