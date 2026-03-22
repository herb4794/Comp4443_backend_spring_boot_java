package comp4443.booking_app.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comp4443.booking_app.entity.Room;
import comp4443.booking_app.repository.RoomRepository;

@RestController
@RequestMapping("/rooms")
@CrossOrigin
public class RoomController {

  @Autowired
  private RoomRepository roomRepository;

  @GetMapping
  public List<Room> getAll() {
    return roomRepository.findAll();
  }
}
