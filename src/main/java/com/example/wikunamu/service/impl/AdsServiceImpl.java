package com.example.wikunamu.service.impl;

import com.example.wikunamu.dto.AdsDTO;
import com.example.wikunamu.dto.AppUserDTO;
import com.example.wikunamu.exception.CustomException;
import com.example.wikunamu.model.AdsDetail;
import com.example.wikunamu.model.AppUser;
import com.example.wikunamu.repository.AdsRepository;
import com.example.wikunamu.repository.UserRepository;
import com.example.wikunamu.service.AdsService;
import com.example.wikunamu.util.ErrorResponse;
import com.example.wikunamu.util.ResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdsServiceImpl implements AdsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdsServiceImpl.class);

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    public AdsServiceImpl(AdsRepository adsRepository, UserRepository userRepository) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> save(AdsDTO adsDTO, Principal principal) {
        try {
            Optional<AppUser> byId = userRepository.findById(Integer.parseInt(principal.getName()));
            AdsDetail adsDetail = dTOToEntity(adsDTO);
            adsDetail.setAppUser(byId.get());
            adsRepository.save(adsDetail);
            return new ResponseEntity<>(new ResponseModel(HttpStatus.OK.value(), "Ad Details Added", true), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> update(AdsDTO adsDTO) {
        try {
            Optional<AdsDetail> byId = adsRepository.findById(adsDTO.getAd_detail_id(), false);
            if (!byId.isPresent()) {
                return new ResponseEntity<>(new ErrorResponse("Not a existing ads detail. Please provide existing details"), HttpStatus.BAD_REQUEST);
            }
            AdsDetail adsDetail = byId.get();
            adsDetail.setAd_category_name(adsDTO.getAd_category_name());
            adsDetail.setAd_item_name(adsDTO.getAd_item_name());
            adsDetail.setAd_item_condition(adsDTO.getAd_item_condition());
            adsDetail.setAd_title(adsDTO.getAd_title());
            adsDetail.setAd_description(adsDTO.getAd_description());
            adsDetail.setAd_city(adsDTO.getAd_city());
            adsDetail.setAd_price(adsDTO.getAd_price());
            adsDetail.setAd_image(adsDTO.getAd_image());
            adsRepository.save(adsDetail);
            return new ResponseEntity<>(new ResponseModel(HttpStatus.OK.value(), "Ad updated successfully", true), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> remove(int ad_detail_id) {
        try {
            Optional<AdsDetail> byId = adsRepository.findById(ad_detail_id, false);
            if (!byId.isPresent()) {
                return new ResponseEntity<>(new ErrorResponse("Not a existing ads detail. Please provide existing details"), HttpStatus.BAD_REQUEST);
            }
            AdsDetail adsDetail = byId.get();
            adsDetail.setDeleted(true);
            adsRepository.save(adsDetail);
            return new ResponseEntity<>(new ResponseModel(HttpStatus.OK.value(), "Ads removing success", true), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> findAll() {
        try {
            List<AdsDetail> all = adsRepository.findAll(false);
            List<AdsDTO> adsDTOS = toDTOList(all);
            return new ResponseEntity<>(adsDTOS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> findAll(Principal principal) {
        try {
            List<AdsDetail> all = adsRepository.findAll(Integer.parseInt(principal.getName()), false);
            return new ResponseEntity<>(toDTOList(all), HttpStatus.OK);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> findAll(String ad_category_name) {
        try {
            List<AdsDetail> all = adsRepository.findAll(ad_category_name, false);
            return new ResponseEntity<>(toDTOList(all), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public ResponseEntity<?> findById(int ad_detail_id) {
        try {
            Optional<AdsDetail> byId = adsRepository.findById(ad_detail_id, false);
            return byId.<ResponseEntity<?>>map(adsDetail -> new ResponseEntity<>(entityToDTO(adsDetail), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(new ErrorResponse("Not a existing ads detail. Please provide existing details"), HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    private AdsDetail dTOToEntity(AdsDTO adsDTO) {
        try {
            AdsDetail adsDetail = new AdsDetail();
            adsDetail.setAd_detail_id(adsDTO.getAd_detail_id());
            adsDetail.setAd_category_name(adsDTO.getAd_category_name());
            adsDetail.setAd_item_name(adsDTO.getAd_item_name());
            adsDetail.setAd_item_condition(adsDTO.getAd_item_condition());
            adsDetail.setAd_title(adsDTO.getAd_title());
            adsDetail.setAd_description(adsDTO.getAd_description());
            adsDetail.setAd_city(adsDTO.getAd_city());
            adsDetail.setAd_price(adsDTO.getAd_price());
            adsDetail.setAd_image(adsDTO.getAd_image());
            return adsDetail;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }

    }

    private AdsDTO entityToDTO(AdsDetail adsDetail) {
        try {
            AdsDTO adsDTO = new AdsDTO();
            adsDTO.setAd_detail_id(adsDetail.getAd_detail_id());
            adsDTO.setAd_category_name(adsDetail.getAd_category_name());
            adsDTO.setAd_item_name(adsDetail.getAd_item_name());
            adsDTO.setAd_item_condition(adsDetail.getAd_item_condition());
            adsDTO.setAd_title(adsDetail.getAd_title());
            adsDTO.setAd_description(adsDetail.getAd_description());
            adsDTO.setAd_city(adsDetail.getAd_city());
            adsDTO.setAd_price(adsDetail.getAd_price());
            adsDTO.setAd_image(adsDetail.getAd_image());
            AppUser appUser = adsDetail.getAppUser();
            AppUserDTO appUserDTO = new AppUserDTO();
            appUserDTO.setUser_id(appUser.getUser_id());
            appUserDTO.setUser_name(appUser.getUser_name());
            appUserDTO.setUser_email(appUser.getUser_email());
            appUserDTO.setContact_no(appUser.getContact_no());
            adsDTO.setUser(appUserDTO);
            return adsDTO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    private List<AdsDTO> toDTOList(List<AdsDetail> adsDetails) {
        List<AdsDTO> adsDTOS = new ArrayList<>();
        if (!adsDetails.isEmpty()) {
            for (AdsDetail adsDetail : adsDetails) {
                adsDTOS.add(entityToDTO(adsDetail));
            }
        }
        return adsDTOS;
    }
}
