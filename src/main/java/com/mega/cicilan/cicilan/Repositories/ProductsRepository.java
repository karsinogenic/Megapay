package com.mega.cicilan.cicilan.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mega.cicilan.cicilan.Models.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    @Query("select p from Products p where tier_code = ?1")
    public List<Products> findProductByTierCode(String tier_code);

}
