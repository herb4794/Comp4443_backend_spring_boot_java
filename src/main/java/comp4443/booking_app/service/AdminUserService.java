package comp4443.booking_app.service;

import comp4443.booking_app.dto.AdminUserDto;
import comp4443.booking_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AdminUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();
    }
}
