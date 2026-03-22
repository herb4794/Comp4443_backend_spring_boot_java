package comp4443.booking_app.repository;

import java.util.*;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import comp4443.booking_app.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  @Query("""
         SELECT b FROM Booking b
            WHERE b.roomId = :roomId
            AND b.checkIn < :checkOut
            AND b.checkOut > :checkIn
      """)
  List<Booking> findOverlappingBookings(
      Long roomId,
      LocalDate checkIn,
      LocalDate checkOut);

  List<Booking> findByUserId(Long userId);

  boolean existsByRoomId(Long roomId);

  
}
