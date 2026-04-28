package com.example.users.information.domain.model.aggregates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.example.users.information.domain.model.commands.CreateUserCommand;
import com.example.users.information.domain.model.commands.UpdateUserCommand;
import com.example.users.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User entity representing a system user")
public class User extends AuditableAbstractAggregateRoot<User> {

  @Column(nullable = false, length = 100)
  @NotBlank(message = "Name is required")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
  @Pattern(regexp = "^[a-zA-ZáéíóúñÑ\\s]+$", message = "Name can only contain letters and spaces")
  @Schema(description = "User's full name",
      example = "John Doe",
      minLength = 2,
      maxLength = 100,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @Column(nullable = false, unique = true, length = 120)
  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
  @Size(max = 120, message = "Email must not exceed 120 characters")
  @Schema(description = "User's email address (must be unique)",
      example = "john.doe@example.com",
      pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
      maxLength = 120,
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String email;

  @Column(name = "image_url", length = 500)
  @Schema(description = "URL of the user's profile image",
      example = "https://example.com/images/avatar.jpg",
      maxLength = 500,
      nullable = true)
  private String imageUrl;

  @Column(name = "public_id", length = 200)
  @Schema(description = "Cloud storage public ID for the user's image",
      example = "users/123/avatar-20241201",
      maxLength = 200,
      nullable = true)
  private String publicId;

  public User(CreateUserCommand command) {
    this.name = command.name();
    this.email = command.email();
  }

  public void update(UpdateUserCommand command) {
    this.name = command.name();
    this.email = command.email();
  }

  public void updateImageInfo(String imageUrl, String publicId) {
    this.imageUrl = imageUrl;
    this.publicId = publicId;
  }

  public void removeImage() {
    this.imageUrl = null;
    this.publicId = null;
  }

  public boolean hasImage() {
    return this.imageUrl != null && this.publicId != null;
  }
}
