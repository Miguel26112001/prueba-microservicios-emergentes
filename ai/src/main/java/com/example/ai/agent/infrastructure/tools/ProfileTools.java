package com.example.ai.agent.infrastructure.tools;

import com.example.ai.agent.domain.model.responses.ProfilesResource;
import com.example.ai.agent.infrastructure.clients.profiles.ProfilesClient;
import com.example.ai.agent.infrastructure.clients.profiles.requests.CreateProfileRequest;
import com.example.ai.agent.infrastructure.clients.profiles.requests.UpdateProfileRequest;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProfileTools {

  private final ProfilesClient profilesClient;

  public ProfileTools(ProfilesClient profilesClient) {
    this.profilesClient = profilesClient;
  }

  @Tool(description = "Obtiene un perfil por su ID")
  public String getProfileById(Long profileId) {

    if (profileId == null || profileId <= 0) {
      return "Necesito un ID de usuario válido.";
    }

    try {
      log.info("Tool getProfileById -> profileId={}", profileId);

      ProfilesResource user = profilesClient.getProfileById(profileId);

      return "Encontré al usuario: " + formatUser(user);

    } catch (FeignException.NotFound e) {
      log.warn("Usuario no encontrado -> id={}", profileId);
      return "No encontré ningún usuario con ID " + profileId + ".";

    } catch (Exception e) {
      log.error("Error obteniendo usuario por ID {}", profileId, e);
      return "Ocurrió un problema al buscar el usuario.";
    }
  }

  @Tool(description = "Obtiene un usuario por su correo electrónico")
  public String getProfileByEmail(String email) {

    if (isBlank(email)) {
      return "Necesito un correo electrónico.";
    }

    try {
      log.info("Tool getProfileByEmail -> email={}", email);

      ProfilesResource user = profilesClient.getProfileByEmail(email.trim());

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
  public String getAllProfiles() {

    try {
      log.info("Tool getAllProfiles");

      List<ProfilesResource> users = profilesClient.getAllProfiles();

      if (users == null || users.isEmpty()) {
        return "No hay usuarios registrados.";
      }

      StringBuilder sb = new StringBuilder("Usuarios encontrados:\n");

      for (ProfilesResource user : users) {
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
  public String createProfile(String name, String email) {

    if (isBlank(name)) {
      return "Necesito el nombre del usuario.";
    }

    if (isValidEmail(email)) {
      return "Necesito un correo electrónico válido.";
    }

    try {
      log.info("Tool createProfile -> name={}, email={}", name, email);

      CreateProfileRequest request =
          new CreateProfileRequest(name.trim(), email.trim());

      ProfilesResource created = profilesClient.createProfile(request);

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
  public String updateProfile(Long userId, String name, String email) {

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
      log.info("Tool updateProfile -> id={}, name={}, email={}", userId, name, email);

      UpdateProfileRequest request =
          new UpdateProfileRequest(name.trim(), email.trim());

      ProfilesResource updated = profilesClient.updateProfile(userId, request);

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
  public String deleteProfile(Long userId) {

    if (userId == null || userId <= 0) {
      return "Necesito un ID válido para eliminar.";
    }

    try {
      log.info("Tool deleteProfile -> id={}", userId);

      profilesClient.deleteProfile(userId);

      return "Usuario con ID " + userId + " eliminado correctamente.";

    } catch (FeignException.NotFound e) {
      log.warn("Usuario no encontrado para delete -> id={}", userId);
      return "No encontré ningún usuario con ID " + userId + ".";

    } catch (Exception e) {
      log.error("Error eliminando usuario {}", userId, e);
      return "Ocurrió un problema al eliminar el usuario.";
    }
  }

  private String formatUser(ProfilesResource user) {
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