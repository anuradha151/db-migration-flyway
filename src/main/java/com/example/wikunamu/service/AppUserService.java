package com.example.wikunamu.service;

import com.example.wikunamu.dto.AppUserDTO;
import org.springframework.http.ResponseEntity;

public interface AppUserService {

    ResponseEntity<?> registerUser(AppUserDTO appUserDTO);

    ResponseEntity<?> updateUser(AppUserDTO appUserDTO);

    ResponseEntity<?> removeUser(int user_id);

    ResponseEntity<?> searchUser(int user_id);

    ResponseEntity<?> refreshToken(String refresh_token);

    ResponseEntity<?> loginUser(AppUserDTO appUserDTO);

}
