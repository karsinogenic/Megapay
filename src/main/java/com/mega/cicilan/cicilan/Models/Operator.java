package com.mega.cicilan.cicilan.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "operator")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "nama_operator", unique = true)
    private String nama_operator;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNama_operator() {
        return nama_operator;
    }

    public void setNama_operator(String nama_operator) {
        this.nama_operator = nama_operator;
    }

}
