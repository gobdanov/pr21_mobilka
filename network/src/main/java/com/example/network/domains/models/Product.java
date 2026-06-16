package com.example.network.domains.models;

public class Product {
    public Integer id;
    public String name;
    public String description;
    public Integer gender;
    public String expendture;
    public Integer price;
    public String Img;
    public Integer idUser;
    public Product(String name,
    String description,
    Integer gender,
    String expendture,
    Integer price){
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.expendture = expendture;
        this.price = price;
    }
}
