package com.mega.cicilan.cicilan.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "requests")
public class Requests {

    @Id
    private String Id;

    private String card_nbr;

    private String reff_nbr;

    private String auth_code;

    private String mid;

    private String date;

    private String time;

    private String tenor;

    private String plan_code;

    private String description;

    private String amount;

    // @ColumnDefault(value = "false")
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean is_generated;

    // private Boolean is_sent;

    private String loan_status;

    private LocalDate date_created;

    private LocalDateTime date_time_created;

    private String namafile;

    public String getCard_nbr() {
        return card_nbr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCard_nbr(String card_nbr) {
        this.card_nbr = card_nbr;
    }

    public String getReff_nbr() {
        return reff_nbr;
    }

    public void setReff_nbr(String reff_nbr) {
        this.reff_nbr = reff_nbr;
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTenor() {
        return tenor;
    }

    public void setTenor(String tenor) {
        this.tenor = tenor;
    }

    public String getPlan_code() {
        return plan_code;
    }

    public void setPlan_code(String plan_code) {
        this.plan_code = plan_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIs_generated() {
        return is_generated;
    }

    public void setIs_generated(Boolean is_generated) {
        this.is_generated = is_generated;
    }

    // public Boolean getIs_sent() {
    // return is_sent;
    // }

    // public void setIs_sent(Boolean is_sent) {
    // this.is_sent = is_sent;
    // }

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public String getNamafile() {
        return namafile;
    }

    public void setNamafile(String namafile) {
        this.namafile = namafile;
    }

}
