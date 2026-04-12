package comp4443.booking_app.dto;

public class AdminUserDto {
  private Long id;
  private String email;
  private Integer role;

  public AdminUserDto() {
  }

  public AdminUserDto(Long id, String email, Integer role) {
    this.id = id;
    this.email = email;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public Integer getRole() {
    return role;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setRole(Integer role) {
    this.role = role;
  }
}
