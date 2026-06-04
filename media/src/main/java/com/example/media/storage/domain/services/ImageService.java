package com.example.media.storage.domain.services;

import com.example.media.storage.domain.model.commands.DeleteProfileImageCommand;
import com.example.media.storage.domain.model.commands.UploadProfileImageCommand;
import com.example.media.storage.domain.model.responses.ImageUploadResponse;

public interface ImageService {

  ImageUploadResponse handle(UploadProfileImageCommand command);

  void handle(DeleteProfileImageCommand command);
}
