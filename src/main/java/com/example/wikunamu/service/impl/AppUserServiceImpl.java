package com.example.wikunamu.service.impl;

import com.example.wikunamu.configuration.JWTParameter;
import com.example.wikunamu.dto.AppUserDTO;
import com.example.wikunamu.exception.CustomException;
import com.example.wikunamu.jwt.JwtGenerator;
import com.example.wikunamu.model.AppUser;
import com.example.wikunamu.model.AuthToken;
import com.example.wikunamu.model.enums.UserRole;
import com.example.wikunamu.repository.UserRepository;
import com.example.wikunamu.service.AppUserService;
import com.example.wikunamu.util.ErrorResponse;
import com.example.wikunamu.util.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public ResponseEntity<?> registerUser(AppUserDTO appUserDTO) {
        Optional<AppUser> userByEmail = userRepository.getUserByEmail(appUserDTO.getUser_email());
        if (userByEmail.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("Existing user"), HttpStatus.BAD_REQUEST);
        }
        try {
            userRepository.save(dTOToEntity(appUserDTO));
            return new ResponseEntity<>(new ResponseModel(HttpStatus.OK.value(), "User added successfully", true), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Failed to save user");
        }
    }

    @Override
    public ResponseEntity<?> updateUser(AppUserDTO appUserDTO) {
        try {
            Optional<AppUser> optional = userRepository.findById(appUserDTO.getUser_id());
            if (optional.isPresent()) {
                AppUser appUser = dTOToEntity(appUserDTO);
                appUser.setUser_id(optional.get().getUser_id());
                if (userRepository.save(appUser) != null) {
                    return new ResponseEntity<>("App user updated successfully", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("App user update failed", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            throw new CustomException("Update unsuccessful");
        }

    }

    @Override
    public ResponseEntity<?> removeUser(int user_id) {
        try {
            Optional<AppUser> optional = userRepository.findById(user_id);
            if (optional.isPresent()) {
                userRepository.deleteById(user_id);
                return new ResponseEntity<>("User successfully removed", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            throw new CustomException("Data removal unsuccessful");
        }
    }

    @Override
    public ResponseEntity<?> searchUser(int user_id) {
        try {
            Optional<AppUser> optional = userRepository.findById(user_id);
            if (optional.isPresent()) {
                AppUserDTO appUserDTO = entityToDTO(optional.get());
                return new ResponseEntity<>(appUserDTO, HttpStatus.OK);
            } else {

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            throw new CustomException("Error occurred while fetching data");
        }
    }

    @Override
    public ResponseEntity<?> loginUser(AppUserDTO appUserDTO) {
        Optional<AppUser> appUser = userRepository.validateUser(appUserDTO.getUser_email());
        if (!appUser.isPresent()) {
            return new ResponseEntity<>("Invalid login Credentials!", HttpStatus.UNAUTHORIZED);
        } else if (bCryptPasswordEncoder.matches(appUserDTO.getUser_password(), appUser.get().getUser_password())) {
            String accessToken = createJwtWithoutPrefix(appUser.get());
            String refreshToken = createRefreshToken(appUser.get());
            AuthToken authToken = new AuthToken();
            authToken.setAccess_token(accessToken);
            authToken.setRefresh_token(refreshToken);
            return new ResponseEntity<>(authToken, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid login Credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<?> refreshToken(String refresh_token) {
        try {
            Optional<AppUser> byRefreshToken = userRepository.findByRefreshToken(refresh_token);
            if (!byRefreshToken.isPresent()) {
                return new ResponseEntity<>("Not a existing token", HttpStatus.UNAUTHORIZED);
            }
            AuthToken authToken = createAuthToken(createJwtWithoutPrefix(byRefreshToken.get()), createRefreshToken(byRefreshToken.get()));
            return new ResponseEntity<>(authToken, HttpStatus.OK);
        } catch (Exception e) {
            throw new CustomException("Cannot obtain a new access token");
        }
    }

    private String createJwtWithoutPrefix(AppUser appUser) {
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + appUser.getUser_role()));
        return JwtGenerator.generateAccessJWT(Integer.toString(appUser.getUser_id()), appUser.getUser_email(), grantedAuthorities, JWTParameter.ACCESS_TOKEN_EXPIRATION, JWTParameter.JWT_SECRET);
    }

    private String createRefreshToken(AppUser appUser) {
        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + appUser.getUser_role()));
        String refreshToken = JwtGenerator.generateRefreshToken(Integer.toString(appUser.getUser_id()), appUser.getUser_email(), grantedAuthorityList, JWTParameter.REFRESH_TOKEN_EXPIRATION, JWTParameter.JWT_SECRET);
        userRepository.updateRefreshToken(appUser.getUser_email(), refreshToken);
        return refreshToken;
    }


    private AppUser dTOToEntity(AppUserDTO appUserDTO) {
        AppUser appUser = new AppUser();
        appUser.setUser_name(appUserDTO.getUser_name());
        appUser.setUser_email(appUserDTO.getUser_email());
        appUser.setUser_password(bCryptPasswordEncoder.encode(appUserDTO.getUser_password()));
        appUser.setUser_role(UserRole.ADMIN.toString());
        appUser.setRefresh_token(appUserDTO.getRefresh_token());
        appUser.setContact_no(appUserDTO.getContact_no());
        return appUser;
    }

    private AppUserDTO entityToDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUser_name(appUser.getUser_name());
        appUserDTO.setUser_email(appUser.getUser_email());
        appUserDTO.setUser_role(appUser.getUser_role());
        appUserDTO.setRefresh_token(appUser.getRefresh_token());
        appUserDTO.setContact_no(appUser.getContact_no());
        return appUserDTO;
    }

    private AuthToken createAuthToken(String accessToken, String refreshToken) {
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token(accessToken);
        authToken.setRefresh_token(refreshToken);
        return authToken;
    }

}
