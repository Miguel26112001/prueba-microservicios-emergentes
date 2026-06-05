package com.example.media.storage.interfaces.rest.controllers;

import com.example.media.storage.domain.model.commands.DeleteProfileImageCommand;
import com.example.media.storage.domain.model.commands.UploadProfileImageCommand;
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
      value = "/profiles/{profileId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public ResponseEntity<ImageUploadResource> uploadProfileImage(
      @PathVariable Long profileId,
      @RequestParam("file") MultipartFile file
  ) {

    var command = new UploadProfileImageCommand(profileId, file);

    var response = imageService.handle(command);

    var resource =
        ImageUploadResourceFromResponseAssembler.toResource(response);

    return ResponseEntity.ok(resource);
  }

  @DeleteMapping("/profiles/{profileId}")
  public ResponseEntity<Void> deleteProfileImage(
      @Parameter(description = "Profile ID image to delete", example = "1", required = true)
      @PathVariable Long profileId
  ) {

    var command = new DeleteProfileImageCommand(profileId);

    imageService.handle(command);

    return ResponseEntity.noContent().build();
  }
}