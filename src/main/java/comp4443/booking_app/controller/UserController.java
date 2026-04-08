package comp4443.booking_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import comp4443.booking_app.entity.User;
import comp4443.booking_app.repository.UserRepository;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@RestController
public class UserController {

  @Autowired
  private UserRepository userRepository;

  // REGISTER
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String, Object> body) {

    String email = (String) body.get("email");
    String password = (String) body.get("password");
    Integer role = (Integer) body.get("role");

    // check exist
    if (userRepository.findByUsername(email).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Email already exists"));
    }

    // create user
    User user = new User();
    user.setUsername(email); // 🔥 email → username
    user.setPassword(password);
    user.setRole(role != null ? role : 1);

    userRepository.save(user);

    return ResponseEntity.ok(Map.of(
        "email", email));
  }

  // LOGIN

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpSession session) {
    String email = body.get("email");
    String password = body.get("password");

    User user = userRepository.findByUsername(email).orElse(null);

    if (user == null) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "User not found"));
    }

    if (!user.getPassword().equals(password)) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Incorrect password"));
    }

    session.setAttribute("loggedInUser", user.getUsername());
    session.setAttribute("userRole", user.getRole());

    return ResponseEntity.ok(Map.of(
        "message", "Login successful",
        "email", user.getUsername(),
        "role", user.getRole()));
  }

  @GetMapping("/me")
  public ResponseEntity<?> me(HttpSession session) {
    String email = (String) session.getAttribute("loggedInUser");
    Integer role = (Integer) session.getAttribute("userRole");

    if (email == null) {
      return ResponseEntity.status(401).body(Map.of(
          "error", "Not logged in"));
    }

    return ResponseEntity.ok(Map.of(
        "email", email,
        "role", role));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> signout(HttpSession session) {
    session.invalidate();

    return ResponseEntity.ok(Map.of(
        "message", "Signed out successfully"));
  }

  @GetMapping("/session-info")
  public ResponseEntity<?> sessionInfo(HttpSession session) {
    return ResponseEntity.ok(Map.of(
        "sessionId", session.getId(),
        "loggedInUser", session.getAttribute("loggedInUser"),
        "userRole", session.getAttribute("userRole"),
        "isNew", session.isNew()));
  }
}
