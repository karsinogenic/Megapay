package com.mega.cicilan.cicilan.Models;

import java.math.BigDecimal;

public class AsccendResponse {
    private Long refId;

    // private SCNTFINP scntfinp;

    private String status;

    private String cardNumber;

    private String authCode;

    private Float amount;

    private String paymentScheme;

    private String description;

    // private Integer authDate;

    // private Integer deliveryDate;

    private Integer billingCycle;

    private String plan;

    private Integer postingDate;

    private Integer installmentBill;

    private Integer monthlyBill;

    private BigDecimal interestRate;

    public Long getRefId() {
        return refId;
    }

    public void setRefId(Long refId) {
        this.refId = refId;
    }

    // public SCNTFINP getScntfinp() {
    // return scntfinp;
    // }

    // public void setScntfinp(SCNTFINP scntfinp) {
    // this.scntfinp = scntfinp;
    // }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getPaymentScheme() {
        return paymentScheme;
    }

    public void setPaymentScheme(String paymentScheme) {
        this.paymentScheme = paymentScheme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // public Integer getAuthDate() {
    // return authDate;
    // }

    // public void setAuthDate(Integer authDate) {
    // this.authDate = authDate;
    // }

    // public Integer getDeliveryDate() {
    // return deliveryDate;
    // }

    // public void setDeliveryDate(Integer deliveryDate) {
    // this.deliveryDate = deliveryDate;
    // }

    public Integer getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(Integer billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Integer getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Integer postingDate) {
        this.postingDate = postingDate;
    }

    public Integer getInstallmentBill() {
        return installmentBill;
    }

    public void setInstallmentBill(Integer installmentBill) {
        this.installmentBill = installmentBill;
    }

    public Integer getMonthlyBill() {
        return monthlyBill;
    }

    public void setMonthlyBill(Integer monthlyBill) {
        this.monthlyBill = monthlyBill;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

}
