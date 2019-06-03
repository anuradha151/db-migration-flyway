package com.example.wikunamu.service;

import com.example.wikunamu.dto.AdsDTO;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

public interface AdsService {

    ResponseEntity<?> save(AdsDTO adsDTO, Principal principal);

    ResponseEntity<?> update(AdsDTO adsDTO);

    ResponseEntity<?> remove(int ad_detail_id);

    ResponseEntity<?> findAll();

    ResponseEntity<?> findAll(Principal principal);

    ResponseEntity<?> findAll(String ad_category_name);

    ResponseEntity<?> findById(int ad_detail_id);

}
