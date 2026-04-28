package com.example.media.storage.interfaces.rest.controllers;

import com.example.media.storage.domain.model.commands.DeleteUserImageCommand;
import com.example.media.storage.domain.model.commands.UploadUserImageCommand;
import com.example.media.storage.domain.services.ImageService;
import com.example.media.storage.interfaces.rest.resources.ImageUploadResource;
import com.example.media.storage.interfaces.rest.transform.ImageUploadResourceFromResponseAssembler;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(
    value = "/api/v1/images",
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class ImageController {

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @PutMapping(
      value = "/users/{userId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ImageUploadResource> uploadUserImage(
      @PathVariable Long userId,
      @RequestParam("file") MultipartFile file
  ) {

    var command = new UploadUserImageCommand(userId, file);

    var response = imageService.handle(command);

    var resource =
        ImageUploadResourceFromResponseAssembler.toResource(response);

    return ResponseEntity.ok(resource);
  }

  @DeleteMapping("/users/{userId}")
  public ResponseEntity<Void> deleteUserImage(
      @Parameter(description = "User ID image to delete", example = "1", required = true)
      @PathVariable Long userId
  ) {

    var command = new DeleteUserImageCommand(userId);

    imageService.handle(command);

    return ResponseEntity.noContent().build();
  }
}