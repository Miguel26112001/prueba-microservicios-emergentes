package com.example.authentication.management.infrastructure.hashing.bcrypt;

import com.example.authentication.management.application.internal.outboundservices.hashing.HashingService;
import com.example.authentication.management.infrastructure.hashing.bcrypt.services.HashingServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This interface is a marker interface for the BCrypt hashing service.
 * It extends the {@link HashingService} and {@link PasswordEncoder} interfaces.
 * This interface is used to inject the BCrypt hashing service in the {@link HashingServiceImpl} class.
 */
public interface BcryptHashingService extends HashingService, PasswordEncoder {
}