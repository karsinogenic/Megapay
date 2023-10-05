package com.mega.cicilan.cicilan.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mega.cicilan.cicilan.Models.Requests;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Requests, String> {

    @Query("select r from Requests r where r.is_generated=?1")
    List<Requests> findByIs_generated(Boolean is_generated);

    @Query("select r from Requests r where r.card_nbr=:card_nbr and r.auth_code=:auth")
    Optional<Requests> findByCardAuth(@Param("card_nbr") String card_nbr, @Param("auth") String auth);

    Optional<Requests> findById(String id);

}
