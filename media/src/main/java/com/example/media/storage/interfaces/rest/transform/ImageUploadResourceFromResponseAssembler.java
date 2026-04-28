package com.example.media.storage.interfaces.rest.transform;

import com.example.media.storage.domain.model.responses.ImageUploadResponse;
import com.example.media.storage.interfaces.rest.resources.ImageUploadResource;

public class ImageUploadResourceFromResponseAssembler {

  private ImageUploadResourceFromResponseAssembler() {
  }

  public static ImageUploadResource toResource(ImageUploadResponse response) {

    return new ImageUploadResource(
        response.imageUrl(),
        response.publicId()
    );
  }
}
