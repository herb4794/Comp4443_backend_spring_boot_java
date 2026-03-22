package comp4443.booking_app.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comp4443.booking_app.entity.Booking;
import comp4443.booking_app.repository.BookingRepository;
import comp4443.booking_app.repository.RoomRepository;
import comp4443.booking_app.repository.UserRepository;

@Service
public class BookingService {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private UserRepository userRepository;

  public Booking createBooking(Booking booking) {

    if (!roomRepository.existsById(booking.getRoomId())) {
      throw new RuntimeException("Room not found");
    }

    if (!userRepository.existsById(booking.getRoomId())) {
      throw new RuntimeException("User not found");
    }
    List<Booking> overlaps = bookingRepository.findOverlappingBookings(
        booking.getRoomId(), booking.getCheckIn(), booking.getCheckOut());

    if (!overlaps.isEmpty()) {
      throw new RuntimeException("Room already booked in this time range");
    }

    return bookingRepository.save(booking);
  }

  public List<Booking> getUserBookings(Long userId) {
    return bookingRepository.findByUserId(userId);
  }

  public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }
}
