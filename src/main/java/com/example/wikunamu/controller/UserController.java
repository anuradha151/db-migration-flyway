package com.example.wikunamu.controller;

import com.example.wikunamu.dto.AppUserDTO;
import com.example.wikunamu.exception.CustomException;
import com.example.wikunamu.exception.CustomValidateException;
import com.example.wikunamu.model.AuthToken;
import com.example.wikunamu.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final AppUserService appUserService;
    private final CustomValidateException customValidateException;
    private String validateError;


    public UserController(AppUserService appUserService, CustomValidateException customValidateException) {
        this.appUserService = appUserService;
        this.customValidateException = customValidateException;
    }

    @PostMapping("/save")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody AppUserDTO appUserDTO, BindingResult bindingResult) {
        LOGGER.info("HIT - /user/save | payload : {}", appUserDTO);
        if (bindingResult.hasErrors()) {
            validateError = customValidateException.validationException(bindingResult);
            throw new CustomException(validateError);
        }

        return appUserService.registerUser(appUserDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody AppUserDTO cmsUserDTO, BindingResult bindingResult) {
        LOGGER.info("HIT - /user/update | payload : {}", cmsUserDTO);
        if (bindingResult.hasErrors()) {
            validateError = customValidateException.validationException(bindingResult);
            throw new CustomException(validateError);
        }
        return appUserService.updateUser(cmsUserDTO);
    }

    @DeleteMapping("/delete/{user_id:.+}")
    public ResponseEntity<?> deleteUser(@PathVariable int user_id) {
        LOGGER.info("HIT - /user/delete | user_id : {}", user_id);
        return appUserService.removeUser(user_id);
    }

    @GetMapping("/search/{user_id:.+}")
    public ResponseEntity<?> searchUser(@PathVariable int user_id) {
        LOGGER.info("HIT - /user/search | user_id : {}", user_id);
        return appUserService.searchUser(user_id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AppUserDTO appUserDTO) {
        LOGGER.info("HIT - /user/login | payload : {}", appUserDTO);
        return appUserService.loginUser(appUserDTO);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> getAccessToken(@RequestBody AuthToken token) {
        LOGGER.info("HIT - /user/refresh_token");
        return appUserService.refreshToken(token.getRefresh_token());
    }

}
