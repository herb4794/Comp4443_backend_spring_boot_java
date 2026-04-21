package comp4443.booking_app.controller;

import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/test")

public class DBTestController {
  @GetMapping("/db")

  public Map<String, Object> testDB() {

    Map<String, Object> result = new HashMap<>();

    try {
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/comp4442",
          "root",
          "");

      result.put("status", "connected");

      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM users LIMIT 2");            
      if (rs.next()) {
        result.put("name = ", rs.getString("name"));
      
      }

      conn.close();

    } catch (Exception e) {
      result.put("status", "error");
      result.put("message", e.getMessage());
    }
    return result;

  }

}
