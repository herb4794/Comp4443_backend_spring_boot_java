package comp4443.booking_app.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import comp4443.booking_app.entity.Booking;
import comp4443.booking_app.service.BookingService;

@CrossOrigin
@RestController
@RequestMapping("/bookings")

public class BookingController {

  @Autowired
  private BookingService bookingService;

  // NOTE: Create Booking
  @PostMapping
  public ResponseEntity<?> create(@RequestBody Booking booking) {
    System.out.println("ROOM ID: " + booking.getRoomId());
    System.out.println("USER ID: " + booking.getUserId());
    
    try {
      Booking saved = bookingService.createBooking(booking);
      return ResponseEntity.ok(saved);
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
  }

  // NOTE: user booking
  @GetMapping("/my")
  public List<Booking> my(@RequestPart Long userId) {
    return bookingService.getUserBookings(userId);
  }

  // NOTE: admin
  @GetMapping("/all")
  public List<Booking> all() {
    return bookingService.getAllBookings();
  }

}
