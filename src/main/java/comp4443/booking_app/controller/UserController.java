package comp4443.booking_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import comp4443.booking_app.entity.User;
import comp4443.booking_app.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {

    String email = (String) body.get("email");
    String password = (String) body.get("password");
    Integer role = (Integer) body.get("role");

    if (email == null || email.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Email is required"));
    }

    if (password == null || password.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Password is required"));
    }

    if (userRepository.findByUsername(email).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Email already exists"));
    }

    User user = new User();
    user.setUsername(email);
    user.setPassword(password);
    user.setRole(role != null ? role : 1);

    userRepository.save(user);

    return ResponseEntity.ok(Map.of(
        "message", "Register successful",
        "email", user.getUsername(),
        "role", user.getRole()));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> body,
      HttpSession session) {

    String email = body.get("email");
    String password = body.get("password");

    if (email == null || email.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Email is required"));
    }

    if (password == null || password.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Password is required"));
    }

    User user = userRepository.findByUsername(email).orElse(null);

    if (user == null) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "User with email " + email + " not found"));
    }

    if (!user.getPassword().equals(password)) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Incorrect password"));
    }

    session.setAttribute("loggedInUser", user.getUsername());
    session.setAttribute("userId", user.getId());
    session.setAttribute("userRole", user.getRole());

    return ResponseEntity.ok(Map.of(
        "message", "Login successful",
        "email", user.getUsername(),
        "userId", user.getId(),
        "role", user.getRole()));
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(HttpSession session) {
    String email = (String) session.getAttribute("loggedInUser");
    Object userIdObj = session.getAttribute("userId");
    Integer role = (Integer) session.getAttribute("userRole");

    if (email == null || userIdObj == null || role == null) {
      return ResponseEntity.status(401).body(Map.of(
          "error", "Not logged in"));
    }

    if (!(userIdObj instanceof Number)) {
      return ResponseEntity.status(500).body(Map.of(
          "error", "Invalid session userId type"));
    }

    Long userId = ((Number) userIdObj).longValue();

    return ResponseEntity.ok(Map.of(
        "email", email,
        "userId", userId,
        "role", role));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> signout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok(Map.of(
        "message", "Signed out"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
    String email = body.get("email");

    if (email == null || email.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Email is required"));
    }

    User user = userRepository.findByUsername(email).orElse(null);

    if (user == null) {
      return ResponseEntity.ok(Map.of(
          "message", "If the account exists, a reset token has been generated."));
    }

    String token = UUID.randomUUID().toString();
    LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

    user.setResetToken(token);
    user.setResetTokenExpiry(expiry);
    userRepository.save(user);

    return ResponseEntity.ok(Map.of(
        "message", "Reset token generated",
        "resetToken", token,
        "expiresAt", expiry.toString()));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
    String token = body.get("token");
    String newPassword = body.get("newPassword");
    String confirmPassword = body.get("confirmPassword");

    if (token == null || token.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Token is required"));
    }

    if (newPassword == null || newPassword.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "New password is required"));
    }

    if (confirmPassword == null || confirmPassword.isBlank()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Confirm password is required"));
    }

    if (!newPassword.equals(confirmPassword)) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Passwords do not match"));
    }

    User user = userRepository.findByResetToken(token).orElse(null);

    if (user == null) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Invalid token"));
    }

    if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Token has expired"));
    }

    user.setPassword(newPassword);
    user.setResetToken(null);
    user.setResetTokenExpiry(null);
    userRepository.save(user);

    return ResponseEntity.ok(Map.of(
        "message", "Password reset successful"));
  }
}
