package com.mega.cicilan.cicilan.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mega.cicilan.cicilan.Models.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

    @Query("select c.channel_name from Channel c where c.channel_code = :kode")
    public String getNamaChannel(@Param("kode") String kode);

}
