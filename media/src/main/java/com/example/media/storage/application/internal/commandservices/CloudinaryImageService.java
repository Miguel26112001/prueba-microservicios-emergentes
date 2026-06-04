package com.example.media.storage.application.internal.commandservices;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.media.storage.application.integration.events.ProfilesEventPublisher;
import com.example.media.storage.domain.model.commands.DeleteProfileImageCommand;
import com.example.media.storage.domain.model.commands.UploadProfileImageCommand;
import com.example.media.storage.domain.model.events.ProfileImageUpdatedEvent;
import com.example.media.storage.domain.model.responses.ImageUploadResponse;
import com.example.media.storage.domain.services.ImageService;
import com.example.media.storage.domain.services.ProfilesExternalService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CloudinaryImageService implements ImageService {

  private final Cloudinary cloudinary;
  private final ProfilesExternalService profilesExternalService;
  private final ProfilesEventPublisher profilesEventPublisher;

  public CloudinaryImageService(
      Cloudinary cloudinary,
      ProfilesExternalService profilesExternalService,
      ProfilesEventPublisher profilesEventPublisher) {
    this.cloudinary = cloudinary;
    this.profilesExternalService = profilesExternalService;
    this.profilesEventPublisher = profilesEventPublisher;
  }

  @Override
  public ImageUploadResponse handle(UploadProfileImageCommand command) {

    var userOptional =
        profilesExternalService.getProfileById(command.userId());

    if (userOptional.isEmpty()) {
      throw new RuntimeException("Profile not found");
    }

    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> result = cloudinary.uploader().upload(
          command.file().getBytes(),
          ObjectUtils.asMap(
              "folder", "users",
              "public_id", command.userId().toString(),
              "overwrite", true,
              "resource_type", "image"
          )
      );

      var imageResponse = mapResponse(result);

      profilesEventPublisher.publishProfileImageUpdated(
          new ProfileImageUpdatedEvent(
              command.userId(),
              imageResponse.imageUrl(),
              imageResponse.publicId()
          )
      );

      return imageResponse;

    } catch (Exception e) {
      throw new RuntimeException(
          "Error uploading image for profile id: " + command.userId(),
          e
      );
    }
  }

  @Override
  public void handle(DeleteProfileImageCommand command) {

    var userOptional =
        profilesExternalService.getProfileById(command.userId());

    if (userOptional.isEmpty()) {
      throw new RuntimeException("User not found");
    }

    String publicId = "users/" + command.userId();

    try {
      cloudinary.uploader().destroy(
          publicId,
          ObjectUtils.emptyMap()
      );
    } catch (Exception e) {
      throw new RuntimeException(
          "Error deleting image with public id: " + publicId,
          e
      );
    }
  }

  private ImageUploadResponse mapResponse(Map<String, Object> result) {
    return new ImageUploadResponse(
        result.get("secure_url").toString(),
        result.get("public_id").toString()
    );
  }
}
