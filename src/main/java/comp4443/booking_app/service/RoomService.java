package comp4443.booking_app.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comp4443.booking_app.repository.BookingRepository;
import comp4443.booking_app.repository.RoomRepository;

@Service
public class RoomService {

  @Autowired
  private RoomRepository roomRepo;

  @Autowired
  private BookingRepository bookingRepo;

  public void deleteRoom(Long roomId) {

    boolean hasBooking = bookingRepo.existsByRoomId(roomId);

    if (hasBooking) {
      throw new RuntimeException("Cannot delete room: already booked !");

    }

    roomRepo.deleteById(roomId);
  }
}
