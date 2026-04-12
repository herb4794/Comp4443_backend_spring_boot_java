package comp4443.booking_app.repository;

import java.util.List;
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
      LocalDate checkOut
  );

  @Query("""
         SELECT b FROM Booking b
         WHERE b.userId = :userId
           AND b.roomId = :roomId
           AND b.checkIn = :checkIn
           AND b.checkOut = :checkOut
      """)
  List<Booking> findDuplicateUserRoomBooking(
      Long userId,
      Long roomId,
      LocalDate checkIn,
      LocalDate checkOut
  );

  List<Booking> findByUserId(Long userId);

  boolean existsByRoomId(Long roomId);
}
