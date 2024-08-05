package com.example.demo8.model;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class EditDto {

    private String id;
    // Getters and setters
    private String name;
    private String email;
    private String sdt;
    private String username;
    private String role;
    private String password; // Only for creating new users or changing passwords


}
