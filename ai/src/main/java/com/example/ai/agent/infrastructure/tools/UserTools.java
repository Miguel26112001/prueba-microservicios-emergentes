package com.example.ai.agent.infrastructure.tools;

import com.example.ai.agent.domain.model.responses.UserResource;
import com.example.ai.agent.infrastructure.clients.users.UsersClient;
import com.example.ai.agent.infrastructure.clients.users.requests.CreateUserRequest;
import com.example.ai.agent.infrastructure.clients.users.requests.UpdateUserRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserTools {

  private final UsersClient usersClient;

  public UserTools(UsersClient usersClient) {
    this.usersClient = usersClient;
  }

  @Tool(description = "Obtiene un usuario por su ID")
  public String getUserById(Long userId) {

    if (userId == null || userId <= 0) {
      return "Necesito un ID de usuario válido.";
    }

    try {
      log.info("Tool getUserById -> userId={}", userId);

      UserResource user = usersClient.getUserById(userId);

      return "Encontré al usuario: " + formatUser(user);

    } catch (FeignException.NotFound e) {
      log.warn("Usuario no encontrado -> id={}", userId);
      return "No encontré ningún usuario con ID " + userId + ".";

    } catch (Exception e) {
      log.error("Error obteniendo usuario por ID {}", userId, e);
      return "Ocurrió un problema al buscar el usuario.";
    }
  }

  @Tool(description = "Obtiene un usuario por su correo electrónico")
  public String getUserByEmail(String email) {

    if (isBlank(email)) {
      return "Necesito un correo electrónico.";
    }

    try {
      log.info("Tool getUserByEmail -> email={}", email);

      UserResource user = usersClient.getUserByEmail(email.trim());

      return "Encontré al usuario: " + formatUser(user);

    } catch (FeignException.NotFound e) {
      log.warn("Usuario no encontrado -> email={}", email);
      return "No encontré ningún usuario con correo " + email + ".";

    } catch (Exception e) {
      log.error("Error obteniendo usuario por email {}", email, e);
      return "Ocurrió un problema al buscar el usuario.";
    }
  }

  @Tool(description = "Lista todos los usuarios registrados")
  public String getAllUsers() {

    try {
      log.info("Tool getAllUsers");

      List<UserResource> users = usersClient.getAllUsers();

      if (users == null || users.isEmpty()) {
        return "No hay usuarios registrados.";
      }

      StringBuilder sb = new StringBuilder("Usuarios encontrados:\n");

      for (UserResource user : users) {
        sb.append("- ")
            .append(formatUser(user))
            .append("\n");
      }

      return sb.toString();

    } catch (Exception e) {
      log.error("Error listando usuarios", e);
      return "No pude obtener la lista de usuarios en este momento.";
    }
  }

  @Tool(description = "Crea un nuevo usuario con nombre y email")
  public String createUser(String name, String email) {

    if (isBlank(name)) {
      return "Necesito el nombre del usuario.";
    }

    if (isValidEmail(email)) {
      return "Necesito un correo electrónico válido.";
    }

    try {
      log.info("Tool createUser -> name={}, email={}", name, email);

      CreateUserRequest request =
          new CreateUserRequest(name.trim(), email.trim());

      UserResource created = usersClient.createUser(request);

      return "Usuario creado correctamente: " + formatUser(created);

    } catch (FeignException.Conflict e) {
      log.warn("Email duplicado -> {}", email);
      return "No pude crear el usuario porque ese correo ya está registrado.";

    } catch (FeignException.BadRequest e) {
      log.warn("Datos inválidos al crear usuario");
      return "Los datos enviados no son válidos para crear el usuario.";

    } catch (Exception e) {
      log.error("Error creando usuario", e);
      return "Ocurrió un problema al crear el usuario.";
    }
  }

  @Tool(description = "Actualiza un usuario existente")
  public String updateUser(Long userId, String name, String email) {

    if (userId == null || userId <= 0) {
      return "Necesito un ID válido para actualizar.";
    }

    if (isBlank(name)) {
      return "Necesito el nuevo nombre del usuario.";
    }

    if (isValidEmail(email)) {
      return "Necesito un correo electrónico válido.";
    }

    try {
      log.info("Tool updateUser -> id={}, name={}, email={}", userId, name, email);

      UpdateUserRequest request =
          new UpdateUserRequest(name.trim(), email.trim());

      UserResource updated = usersClient.updateUser(userId, request);

      return "Usuario actualizado correctamente: " + formatUser(updated);

    } catch (FeignException.NotFound e) {
      log.warn("Usuario no encontrado para update -> id={}", userId);
      return "No encontré ningún usuario con ID " + userId + " para actualizar.";

    } catch (FeignException.Conflict e) {
      log.warn("Email duplicado en update -> {}", email);
      return "No pude actualizar el usuario porque ese correo ya está en uso.";

    } catch (FeignException.BadRequest e) {
      log.warn("Datos inválidos en update");
      return "Los datos enviados no son válidos para actualizar el usuario.";

    } catch (Exception e) {
      log.error("Error actualizando usuario {}", userId, e);
      return "Ocurrió un problema al actualizar el usuario.";
    }
  }

  @Tool(description = "Elimina un usuario por su ID")
  public String deleteUser(Long userId) {

    if (userId == null || userId <= 0) {
      return "Necesito un ID válido para eliminar.";
    }

    try {
      log.info("Tool deleteUser -> id={}", userId);

      usersClient.deleteUser(userId);

      return "Usuario con ID " + userId + " eliminado correctamente.";

    } catch (FeignException.NotFound e) {
      log.warn("Usuario no encontrado para delete -> id={}", userId);
      return "No encontré ningún usuario con ID " + userId + ".";

    } catch (Exception e) {
      log.error("Error eliminando usuario {}", userId, e);
      return "Ocurrió un problema al eliminar el usuario.";
    }
  }

  private String formatUser(UserResource user) {
    return "ID: " + user.id()
        + ", Nombre: " + user.name()
        + ", Email: " + user.email();
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private boolean isValidEmail(String email) {
    return isBlank(email) || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }
}