package com.example.media.storage.application.internal.commandservices;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.media.storage.domain.model.commands.DeleteUserImageCommand;
import com.example.media.storage.domain.model.commands.UploadUserImageCommand;
import com.example.media.storage.domain.model.responses.ImageUploadResponse;
import com.example.media.storage.domain.services.ImageService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CloudinaryImageService implements ImageService {

  private final Cloudinary cloudinary;

  public CloudinaryImageService(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  @Override
  public ImageUploadResponse handle(UploadUserImageCommand command) {
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

      return mapResponse(result);

    } catch (Exception e) {
      throw new RuntimeException(
          "Error uploading image for user id: " + command.userId(),
          e
      );
    }
  }

  @Override
  public void handle(DeleteUserImageCommand command) {
    try {
      cloudinary.uploader().destroy(
          command.publicId(),
          ObjectUtils.emptyMap()
      );
    } catch (Exception e) {
      throw new RuntimeException(
          "Error deleting image with public id: " + command.publicId(),
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
