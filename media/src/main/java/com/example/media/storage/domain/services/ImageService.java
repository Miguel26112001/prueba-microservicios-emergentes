package com.example.media.storage.domain.services;

import com.example.media.storage.domain.model.commands.DeleteUserImageCommand;
import com.example.media.storage.domain.model.commands.UploadUserImageCommand;
import com.example.media.storage.domain.model.responses.ImageUploadResponse;

public interface ImageService {

  ImageUploadResponse handle(UploadUserImageCommand command);

  void handle(DeleteUserImageCommand command);
}
