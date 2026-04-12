package comp4443.booking_app.service;

import java.time.LocalDate;

import java.util.List;

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

    if (booking.getUserId() == null) {
      throw new RuntimeException("User ID is required");
    }

    if (booking.getRoomId() == null) {
      throw new RuntimeException("Room ID is required");
    }

    if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
      throw new RuntimeException("Check-in and check-out are required");
    }

    if (!booking.getCheckIn().isBefore(booking.getCheckOut())) {
      throw new RuntimeException("Check-out must be after check-in");
    }

    if (!roomRepository.existsById(booking.getRoomId())) {
      throw new RuntimeException("Room not found");
    }

    if (!userRepository.existsById(booking.getUserId())) {
      throw new RuntimeException("User not found");
    }

    List<Booking> duplicateBookings = bookingRepository.findDuplicateUserRoomBooking(
        booking.getUserId(),
        booking.getRoomId(),
        booking.getCheckIn(),
        booking.getCheckOut());

    if (!duplicateBookings.isEmpty()) {
      throw new RuntimeException("You already booked this room for the same dates");
    }

    List<Booking> overlaps = bookingRepository.findOverlappingBookings(
        booking.getRoomId(),
        booking.getCheckIn(),
        booking.getCheckOut());

    if (!overlaps.isEmpty()) {
      throw new RuntimeException("Room already booked in this time range");
    }

    return bookingRepository.save(booking);
  }

  public Booking updateBookingDates(Long id, Booking updatedBooking) {
    Booking existing = bookingRepository.findById(id).orElse(null);

    if (existing == null) {
      throw new RuntimeException("Booking not found");
    }

    if (updatedBooking.getCheckIn() == null || updatedBooking.getCheckOut() == null) {
      throw new RuntimeException("Check-in and check-out are required");
    }

    if (!updatedBooking.getCheckIn().isBefore(updatedBooking.getCheckOut())) {
      throw new RuntimeException("Check-out must be after check-in");
    }

    List<Booking> overlaps = bookingRepository.findOverlappingBookings(
        existing.getRoomId(),
        updatedBooking.getCheckIn(),
        updatedBooking.getCheckOut());

    overlaps.removeIf(b -> b.getId().equals(existing.getId()));

    if (!overlaps.isEmpty()) {
      throw new RuntimeException("Room already booked in this time range");
    }

    existing.setCheckIn(updatedBooking.getCheckIn());
    existing.setCheckOut(updatedBooking.getCheckOut());

    return bookingRepository.save(existing);
  }

  public Booking findById(Long id) {
    return bookingRepository.findById(id).orElse(null);
  }

  public void deleteBooking(Long id) {
    bookingRepository.deleteById(id);
  }

  public List<Booking> getUserBookings(Long userId) {
    return bookingRepository.findByUserId(userId);
  }

  public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
  }

  public boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
    if (roomId == null) {
      throw new RuntimeException("Room ID is required");
    }

    if (checkIn == null || checkOut == null) {
      throw new RuntimeException("Check-in and check-out are required");
    }

    if (!checkIn.isBefore(checkOut)) {
      throw new RuntimeException("Check-out must be after check-in");
    }

    if (!roomRepository.existsById(roomId)) {
      throw new RuntimeException("Room not found");
    }

    List<Booking> overlaps = bookingRepository.findOverlappingBookings(
        roomId, checkIn, checkOut);

    return overlaps.isEmpty();
  }
}
