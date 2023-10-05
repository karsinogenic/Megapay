package com.mega.cicilan.cicilan.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String tier_code;

    private Double interest;

    private String interest_channel;

    private String interest_type;

    private String plan_code;

    private Long tenor;

    @Column(columnDefinition = "boolean default true")
    private Boolean is_active;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTier_code() {
        return tier_code;
    }

    public void setTier(String tier_code) {
        this.tier_code = tier_code;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public String getInterest_channel() {
        return interest_channel;
    }

    public void setInterest_channel(String interest_channel) {
        this.interest_channel = interest_channel;
    }

    public String getInterest_type() {
        return interest_type;
    }

    public void setInterest_type(String interest_type) {
        this.interest_type = interest_type;
    }

    public String getPlan_code() {
        return plan_code;
    }

    public void setPlan_code(String plan_code) {
        this.plan_code = plan_code;
    }

    public Long getTenor() {
        return tenor;
    }

    public void setTenor(Long tenor) {
        this.tenor = tenor;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

}
