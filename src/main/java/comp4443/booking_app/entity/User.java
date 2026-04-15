package comp4443.booking_app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private LocalDateTime createdAt = LocalDateTime.now();
  private int role; // 1=user, 0=admin
  private String resetToken;
  private LocalDateTime resetTokenExpiry;

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public int getRole() {
    return role;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getResetToken() {
    return resetToken;
  }

  public LocalDateTime getResetTokenExpiry() {
    return resetTokenExpiry;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRole(int role) {
    this.role = role;
  }

  public void setResetToken(String resetToken) {
    this.resetToken = resetToken;
  }

  public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
    this.resetTokenExpiry = resetTokenExpiry;
  }
}
