package com.example.demo8.DAO;

import com.example.demo8.model.AppUser;
import com.example.demo8.model.AppUser1;

import java.util.List;

public interface AppUserDAO {
    List<AppUser> getAllUsers();
    AppUser getUserById(int id);
    AppUser getUserByUsername(String username);
    void updateUser(AppUser user);
    void deleteUser(int id);
    void saveUser(AppUser user); // Thêm phương thức lưu người dùng
    AppUser authenticateUser(String username, String password);
    void saveUser1(AppUser1 user); // Thêm phương thức lưu người dùng
    int getUserIdByUsername(String username);
}
