package com.duong.salesmanagement.config;

import com.duong.salesmanagement.model.*;
import com.duong.salesmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Initialize Room Types
        if (roomTypeRepository.count() == 0) {
            RoomType standard = new RoomType();
            standard.setTypeName("Standard");
            standard.setCapacity(2);
            standard.setBasePrice(new BigDecimal("500000.00"));

            RoomType deluxe = new RoomType();
            deluxe.setTypeName("Deluxe");
            deluxe.setCapacity(2);
            deluxe.setBasePrice(new BigDecimal("800000.00"));

            RoomType suite = new RoomType();
            suite.setTypeName("Suite");
            suite.setCapacity(4);
            suite.setBasePrice(new BigDecimal("1500000.00"));

            roomTypeRepository.saveAll(Arrays.asList(standard, deluxe, suite));
            System.out.println(">>> Sample Room Types initialized.");
        }

        // 2. Initialize Rooms
        if (roomRepository.count() == 0) {
            RoomType standard = roomTypeRepository.findByTypeName("Standard").orElse(null);
            RoomType deluxe = roomTypeRepository.findByTypeName("Deluxe").orElse(null);
            RoomType suite = roomTypeRepository.findByTypeName("Suite").orElse(null);

            Room r101 = new Room();
            r101.setRoomNumber("101");
            r101.setRoomType(standard);
            r101.setStatus("AVAILABLE");

            Room r102 = new Room();
            r102.setRoomNumber("102");
            r102.setRoomType(standard);
            r102.setStatus("AVAILABLE");

            Room r201 = new Room();
            r201.setRoomNumber("201");
            r201.setRoomType(deluxe);
            r201.setStatus("AVAILABLE");

            Room r301 = new Room();
            r301.setRoomNumber("301");
            r301.setRoomType(suite);
            r301.setStatus("AVAILABLE");

            roomRepository.saveAll(Arrays.asList(r101, r102, r201, r301));
            System.out.println(">>> Sample Rooms initialized.");
        }

        // 3. Initialize Guests
        if (guestRepository.count() == 0) {
            Guest g1 = new Guest();
            g1.setFullName("Nguyen Van A");
            g1.setEmail("vana@example.com");
            g1.setPhone("0901234567");
            g1.setAddress("Hanoi, Vietnam");
            g1.setIdDocument("123456789");

            Guest g2 = new Guest();
            g2.setFullName("Tran Thi B");
            g2.setEmail("thib@example.com");
            g2.setPhone("0912345678");
            g2.setAddress("Ho Chi Minh City, Vietnam");
            g2.setIdDocument("987654321");

            guestRepository.saveAll(Arrays.asList(g1, g2));
            System.out.println(">>> Sample Guests initialized.");
        }

        // 4. Initialize Services
        if (serviceRepository.count() == 0) {
            Service s1 = new Service("Laundry", "LAUNDRY", new BigDecimal("50000.00"));
            s1.setDescription("Professional washing and ironing service.");
            s1.setUnit("per item");

            Service s2 = new Service("Airport Transfer", "TRANSPORT", new BigDecimal("300000.00"));
            s2.setDescription("Private car to/from airport.");
            s2.setUnit("per trip");

            Service s3 = new Service("Breakfast Buffet", "RESTAURANT", new BigDecimal("150000.00"));
            s3.setDescription("International breakfast buffet.");
            s3.setUnit("per person");

            serviceRepository.saveAll(Arrays.asList(s1, s2, s3));
            System.out.println(">>> Sample Services initialized.");
        }

        // 5. Initialize a Sample Reservation
        if (reservationRepository.count() == 0) {
            Guest guest = guestRepository.findAll().get(0);
            Room room = roomRepository.findAll().get(0);

            Reservation res = new Reservation();
            res.setGuest(guest);
            res.setRoom(room);
            res.setCheckInDate(LocalDate.now().plusDays(1));
            res.setCheckOutDate(LocalDate.now().plusDays(3));
            res.setCreatedDate(LocalDate.now());
            res.setConfirmationNumber("CNF-" + System.currentTimeMillis());
            res.setStatus("CONFIRMED");
            res.setNumberOfGuests(2);

            // Calculate total price (2 nights)
            BigDecimal pricePerNight = room.getRoomType().getBasePrice();
            res.setTotalPrice(pricePerNight.multiply(new BigDecimal("2")));

            reservationRepository.save(res);
            System.out.println(">>> Sample Reservation initialized.");
        }
    }
}
