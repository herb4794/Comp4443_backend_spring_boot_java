package comp4443.booking_app.controller;

import java.util.*;

import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/test")

public class TestController {

  @PostMapping
  public Map<String, Object> hello(@RequestBody Map<String, Object> body) {

    System.out.println("Confirm request");

    return Map.of(
        "status", "success",
        "message", "Hellow World",
        "data", body);
  }

}
