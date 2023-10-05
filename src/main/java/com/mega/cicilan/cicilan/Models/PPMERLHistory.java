package com.mega.cicilan.cicilan.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class PPMERLHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String namafile;

    private Boolean is_sent;

    private LocalDate date_created;

    private LocalDateTime date_time_created;

    public String getNamafile() {
        return namafile;
    }

    public void setNamafile(String namafile) {
        this.namafile = namafile;
    }

    public Boolean getIs_sent() {
        return is_sent;
    }

    public void setIs_sent(Boolean is_sent) {
        this.is_sent = is_sent;
    }

    public LocalDate getDate_created() {
        return date_created;
    }

    public void setDate_created(LocalDate date_created) {
        this.date_created = date_created;
    }

    public LocalDateTime getDate_time_created() {
        return date_time_created;
    }

    public void setDate_time_created(LocalDateTime date_time_created) {
        this.date_time_created = date_time_created;
    }

}
