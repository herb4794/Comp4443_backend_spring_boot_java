package comp4443.booking_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")

public class BookingController {

  @GetMapping
  public String test() {
    return "Hello World";
  }
}
