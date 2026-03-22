package comp4443.booking_app.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private Long roomId;
  private LocalDate checkIn;
  private LocalDate checkOut;

  public Long getId() {
    return id;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getRoomId() {
    return roomId;
  }

  public LocalDate getCheckIn() {
    return checkIn;
  }

  public LocalDate getCheckOut() {
    return checkOut;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
  }

  public void setCheckIn(LocalDate checkIn) {
    this.checkIn = checkIn;
  }

  public void setCheckOut(LocalDate checkOut) {
    this.checkOut = checkOut;
  }

}
