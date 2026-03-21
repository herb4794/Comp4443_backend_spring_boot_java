package comp4443.booking_app.controller;

import java.util.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/booking")

public class BookingController {

  @PostMapping
  public void addBooking(@RequestBody Map<String, Object> data) {

    System.out.println("POST RECEIVED");
    System.out.println();
  }

}
