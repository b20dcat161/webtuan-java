package com.example.demo8.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import javax.xml.bind.annotation.*;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;



public class AppUser1 {
    private String username;
    private String email;
    private String phone;
    private String fullname;
    private String role;
    private String password;  // Nếu cần


    public AppUser1(String username, String email, String phone, String fullname, String role,String password) {
        this.username = username;
        this.email=email;
        this.phone=phone;
        this.fullname=fullname;
        this.role=role;
        this.password=password;
    }

    // Getters và Setters

    public AppUser1() {
    }
    public String getUsername() {
        return username;
    }




    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }




    public String getFullname() {
        return fullname;
    }


    public String getRole() {
        return role;
    }



    public String getPassword() {
        return password;
    }


}
