package comp4443.booking_app.controller;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import comp4443.booking_app.entity.Booking;
import comp4443.booking_app.entity.User;
import comp4443.booking_app.repository.UserRepository;
import comp4443.booking_app.service.BookingService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class BookingController {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private UserRepository userRepository;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Booking booking, HttpSession session) {
    String email = (String) session.getAttribute("loggedInUser");

    if (email == null) {
      return ResponseEntity.status(401).body(Map.of(
          "error", "Not logged in"));
    }

    User user = userRepository.findByUsername(email).orElse(null);
    if (user == null) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "User not found"));
    }

    booking.setUserId(user.getId());

    try {
      Booking saved = bookingService.createBooking(booking);
      return ResponseEntity.ok(saved);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", e.getMessage()));
    }
  }

  @GetMapping("/my")
  public ResponseEntity<?> myBookings(HttpSession session) {
    Object userIdObj = session.getAttribute("userId");

    if (userIdObj == null) {
      return ResponseEntity.status(401).body(Map.of(
          "error", "Not logged in"));
    }

    if (!(userIdObj instanceof Number)) {
      return ResponseEntity.status(500).body(Map.of(
          "error", "Invalid session userId type"));
    }

    Long userId = ((Number) userIdObj).longValue();

    List<Booking> bookings = bookingService.getUserBookings(userId);
    return ResponseEntity.ok(bookings);
  }

  @GetMapping("/all")
  public ResponseEntity<?> allBookings(HttpSession session) {
    Integer role = (Integer) session.getAttribute("userRole");

    if (role == null) {
      return ResponseEntity.status(401).body(Map.of(
          "error", "Not logged in"));
    }

    if (role != 0) {
      return ResponseEntity.status(403).body(Map.of(
          "error", "Admin only"));
    }

    return ResponseEntity.ok(bookingService.getAllBookings());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id,
      @RequestBody Booking updatedBooking,
      HttpSession session) {
    Integer role = (Integer) session.getAttribute("userRole");

    if (role == null) {
      return ResponseEntity.status(401).body(Map.of(
          "error", "Not logged in"));
    }

    if (role != 0) {
      return ResponseEntity.status(403).body(Map.of(
          "error", "Admin only"));
    }

    try {
      Booking saved = bookingService.updateBookingDates(id, updatedBooking);
      return ResponseEntity.ok(saved);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", e.getMessage()));
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id, HttpSession session) {
    Integer role = (Integer) session.getAttribute("userRole");
    Object userIdObj = session.getAttribute("userId");

    if (role == null || userIdObj == null) {
      return ResponseEntity.status(401).body(Map.of(
          "error", "Not logged in"));
    }

    if (!(userIdObj instanceof Number)) {
      return ResponseEntity.status(500).body(Map.of(
          "error", "Invalid session userId type"));
    }

    Long currentUserId = ((Number) userIdObj).longValue();

    Booking booking = bookingService.findById(id);
    if (booking == null) {
      return ResponseEntity.status(404).body(Map.of(
          "error", "Booking not found"));
    }

    boolean isAdmin = role == 0;
    boolean isOwner = booking.getUserId().equals(currentUserId);

    if (!isAdmin && !isOwner) {
      return ResponseEntity.status(403).body(Map.of(
          "error", "You can only cancel your own booking"));
    }

    bookingService.deleteBooking(id);

    return ResponseEntity.ok(Map.of(
        "message", "Booking cancelled successfully"));
  }

  @GetMapping("/check")
  public ResponseEntity<?> checkAvailability(
      @RequestParam Long roomId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

    try {
      boolean available = bookingService.isRoomAvailable(roomId, checkIn, checkOut);

      if (available) {
        return ResponseEntity.ok(Map.of(
            "available", true,
            "message", "Room is available"));
      } else {
        return ResponseEntity.ok(Map.of(
            "available", false,
            "message", "This room is already booked in this date range"));
      }

    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", e.getMessage()));
    }
  }

}
