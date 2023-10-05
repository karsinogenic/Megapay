package com.mega.cicilan.cicilan.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mega.cicilan.cicilan.Models.PPMERLHistory;
import java.util.List;

public interface PPMERLHistoryRepository extends JpaRepository<PPMERLHistory, Long> {

    public PPMERLHistory findByNamafile(String namafile);

    @Query("select p from PPMERLHistory p where p.is_sent=null")
    public List<PPMERLHistory> findByIs_sent(Boolean is_sent);
}
