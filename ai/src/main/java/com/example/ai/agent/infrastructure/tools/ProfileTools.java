package com.example.ai.agent.infrastructure.tools;

import com.example.ai.agent.domain.model.responses.ProfilesResource;
import com.example.ai.agent.domain.model.responses.ToolResponse;
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
  public ToolResponse<ProfilesResource> getProfileById(Long profileId) {

    if (profileId == null || profileId <= 0) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "ID de usuario inválido",
          null
      );
    }

    try {

      ProfilesResource profile =
          profilesClient.getProfileById(profileId);

      return new ToolResponse<>(
          true,
          "PROFILE_001",
          "Perfil encontrado",
          profile
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "PROFILE_002",
          "Perfil no encontrado",
          null
      );

    } catch (Exception e) {

      log.error("Error obteniendo perfil {}", profileId, e);

      return new ToolResponse<>(
          false,
          "PROFILE_500",
          "Error interno",
          null
      );
    }
  }

  @Tool(description = "Obtiene un perfil por email")
  public ToolResponse<ProfilesResource> getProfileByEmail(String email) {

    if (isInvalidEmail(email)) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "Email inválido",
          null
      );
    }

    try {

      ProfilesResource profile =
          profilesClient.getProfileByEmail(email.trim());

      return new ToolResponse<>(
          true,
          "PROFILE_001",
          "Perfil encontrado",
          profile
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "PROFILE_002",
          "Perfil no encontrado",
          null
      );

    } catch (Exception e) {

      log.error("Error obteniendo perfil por email {}", email, e);

      return new ToolResponse<>(
          false,
          "PROFILE_500",
          "Error interno",
          null
      );
    }
  }

  @Tool(description = "Lista todos los perfiles")
  public ToolResponse<List<ProfilesResource>> getAllProfiles() {

    try {

      List<ProfilesResource> profiles =
          profilesClient.getAllProfiles();

      return new ToolResponse<>(
          true,
          "PROFILE_001",
          "Perfiles obtenidos",
          profiles
      );

    } catch (Exception e) {

      log.error("Error obteniendo perfiles", e);

      return new ToolResponse<>(
          false,
          "PROFILE_500",
          "Error interno",
          null
      );
    }
  }

  @Tool(description = "Crea un nuevo perfil")
  public ToolResponse<ProfilesResource> createProfile(
      String name,
      String email
  ) {

    if (isBlank(name)) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "Nombre requerido",
          null
      );
    }

    if (isInvalidEmail(email)) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "Email inválido",
          null
      );
    }

    try {

      ProfilesResource profile =
          profilesClient.createProfile(
              new CreateProfileRequest(
                  name.trim(),
                  email.trim()
              )
          );

      return new ToolResponse<>(
          true,
          "PROFILE_003",
          "Perfil creado",
          profile
      );

    } catch (FeignException.Conflict e) {

      return new ToolResponse<>(
          false,
          "PROFILE_409",
          "El email ya existe",
          null
      );

    } catch (Exception e) {

      log.error("Error creando perfil", e);

      return new ToolResponse<>(
          false,
          "PROFILE_500",
          "Error interno",
          null
      );
    }
  }

  @Tool(description = "Actualiza un perfil")
  public ToolResponse<ProfilesResource> updateProfile(
      Long userId,
      String name,
      String email
  ) {

    if (userId == null || userId <= 0) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "ID inválido",
          null
      );
    }

    if (isBlank(name)) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "Nombre requerido",
          null
      );
    }

    if (isInvalidEmail(email)) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "Email inválido",
          null
      );
    }

    try {

      ProfilesResource profile =
          profilesClient.updateProfile(
              userId,
              new UpdateProfileRequest(
                  name.trim(),
                  email.trim()
              )
          );

      return new ToolResponse<>(
          true,
          "PROFILE_004",
          "Perfil actualizado",
          profile
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "PROFILE_002",
          "Perfil no encontrado",
          null
      );

    } catch (FeignException.Conflict e) {

      return new ToolResponse<>(
          false,
          "PROFILE_409",
          "Email ya registrado",
          null
      );

    } catch (Exception e) {

      log.error("Error actualizando perfil {}", userId, e);

      return new ToolResponse<>(
          false,
          "PROFILE_500",
          "Error interno",
          null
      );
    }
  }

  @Tool(description = "Elimina un perfil")
  public ToolResponse<Void> deleteProfile(Long userId) {

    if (userId == null || userId <= 0) {
      return new ToolResponse<>(
          false,
          "PROFILE_400",
          "ID inválido",
          null
      );
    }

    try {

      profilesClient.deleteProfile(userId);

      return new ToolResponse<>(
          true,
          "PROFILE_005",
          "Perfil eliminado",
          null
      );

    } catch (FeignException.NotFound e) {

      return new ToolResponse<>(
          false,
          "PROFILE_002",
          "Perfil no encontrado",
          null
      );

    } catch (Exception e) {

      log.error("Error eliminando perfil {}", userId, e);

      return new ToolResponse<>(
          false,
          "PROFILE_500",
          "Error interno",
          null
      );
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  private boolean isInvalidEmail(String email) {
    return isBlank(email) || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }
}