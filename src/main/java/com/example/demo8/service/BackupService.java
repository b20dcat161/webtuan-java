package com.example.demo8.service;

import com.example.demo8.DAO.AppUserDAOImpl;
import com.example.demo8.model.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class BackupService {

    @Autowired
    private AppUserDAOImpl userRepository;

    public File backupUsers() throws IOException {
        List<AppUser> users = userRepository.getAllUsers();
        ObjectMapper objectMapper = new ObjectMapper();
        File backupFile = new File("backup_users.json");
        objectMapper.writeValue(backupFile, users);
        return backupFile;
    }
}
