package com.example.demo8.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class UserListDTO {
    private List<AppUser1> user1 = new ArrayList<>();

    public void setUsers(List<AppUser1> user1) {
        this.user1 = user1;
    }

    public List<AppUser1> getUser1() {
        return user1;
    }
}