package com.duong.salesmanagement.service;

import com.duong.salesmanagement.model.Guest;
import com.duong.salesmanagement.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Guest getGuestById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Guest ID không được null");
        }

        return guestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách với ID = " + id));
    }

    @Override
    public Guest createGuest(Guest guest) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest không được null");
        }
        return guestRepository.save(guest);
    }

    @Override
    public Guest updateGuest(Long id, Guest guest) {
        if (id == null) {
            throw new IllegalArgumentException("Guest ID không được null");
        }

        Guest existingGuest = getGuestById(id);

        existingGuest.setFullName(guest.getFullName());
        existingGuest.setPhone(guest.getPhone());
        existingGuest.setEmail(guest.getEmail());
        existingGuest.setAddress(guest.getAddress());

        return guestRepository.save(existingGuest);
    }

    @Override
    public void deleteGuest(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Guest ID không được null");
        }
        guestRepository.deleteById(id);
    }

    @Override
    public List<Guest> searchGuest(String keyword) {
        return guestRepository.findByFullNameContainingIgnoreCase(keyword);
    }

}
