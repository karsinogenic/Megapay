package com.mega.cicilan.cicilan.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mega.cicilan.cicilan.Models.Rules;

public interface RulesRepository extends JpaRepository<Rules, Long> {

    @Query("select distinct r.tier_code from Rules r where r.channel_code = ?1")
    public List<String> findDistinctByKode(String channel_code);

    @Query("select r from Rules r where r.tier_code = ?1")
    public List<Rules> findByTier_code(String tier_code);
}
