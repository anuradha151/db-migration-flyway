package com.example.wikunamu.repository;

import com.example.wikunamu.model.AdsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdsRepository extends JpaRepository<AdsDetail, Integer> {

    @Query("SELECT a FROM AdsDetail a WHERE a.deleted = ?1")
    List<AdsDetail> findAll(boolean deleted);


    @Query("SELECT a FROM AdsDetail a WHERE a.appUser.user_id = ?1 AND a.deleted = ?2")
    List<AdsDetail> findAll(int user_id, boolean deleted);

    @Query("SELECT a FROM AdsDetail a WHERE a.ad_category_name = ?1 AND  a.deleted = ?2")
    List<AdsDetail> findAll(String ad_category_name, boolean deleted);


    @Query("SELECT a FROM AdsDetail a WHERE a.ad_detail_id = ?1 AND a.deleted = ?2")
    Optional<AdsDetail> findById(int ad_detail_id, boolean deleted);

}
