package comp4443.booking_app.controller;

import comp4443.booking_app.dto.AdminUserDto;
import comp4443.booking_app.service.AdminUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

  private final AdminUserService adminUserService;

  public AdminUserController(AdminUserService adminUserService) {
    this.adminUserService = adminUserService;
  }

  @GetMapping("/users")
  public List<AdminUserDto> getAllUsers() {
    return adminUserService.getAllUsers();
  }
}
