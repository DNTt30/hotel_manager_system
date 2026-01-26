package com.duong.salesmanagement.service;

import java.util.List;
import com.duong.salesmanagement.model.Guest;

public interface GuestService {

    Guest createGuest(Guest guest);

    Guest getGuestById(Long id);

    List<Guest> getAllGuests();

    Guest updateGuest(Long id, Guest guest);

    void deleteGuest(Long id);

    List<Guest> searchGuest(String keyword);
}
