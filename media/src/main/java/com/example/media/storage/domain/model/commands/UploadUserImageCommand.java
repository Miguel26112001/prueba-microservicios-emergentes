package com.example.media.storage.domain.model.commands;

import org.springframework.web.multipart.MultipartFile;

public record UploadUserImageCommand(
    Long userId,
    MultipartFile file
) {
}
