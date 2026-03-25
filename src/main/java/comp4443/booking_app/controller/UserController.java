package comp4443.booking_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import comp4443.booking_app.entity.User;
import comp4443.booking_app.repository.UserRepository;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
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
  public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

    String email = body.get("email");
    String password = body.get("password");

    User user = userRepository.findByUsername(email)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.getPassword().equals(password)) {
      return ResponseEntity.badRequest().body(Map.of(
          "error", "Wrong password"));
    }

    return ResponseEntity.ok(Map.of(
        "email", user.getUsername(),
        "role", user.getRole()));
  }
}
