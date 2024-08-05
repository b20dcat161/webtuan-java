package com.example.demo8.Controller;

import com.example.demo8.model.AppUser2;
import com.example.demo8.model.LoginDto;
import com.example.demo8.model.RegisterDto;
import com.example.demo8.model.AppUser;
import com.example.demo8.DAO.AppUserDAOImpl; // Import DAO class

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.List;

@Controller
public class AccountController {

    private AppUserDAOImpl appUserDAO2;

    public AccountController(AppUserDAOImpl appUserDAO2) {
        this.appUserDAO2 = appUserDAO2;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    private String backupDir = "\\upload\\a\\";
    @GetMapping("/backup")
    public String backupPage() {
        return "backup";
    }
    @PostMapping("/backup")
    public String backupUsers(RedirectAttributes redirectAttributes) {
        System.out.println(1);
        List<AppUser2> users = appUserDAO2.getAllUsers1();
        File backupFile = new File(backupDir + "users_backup.ser");
        System.out.println(2);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(backupFile))) {
            oos.writeObject(users);
            redirectAttributes.addFlashAttribute("message", "Backup created successfully.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to create backup: " + e.getMessage());
        }

        return "redirect:/backup";
    }

    @GetMapping("/backup/download")
    public ResponseEntity<byte[]> downloadBackup() throws IOException {
        File backupFile = new File(backupDir + "users_backup.ser");

        if (!backupFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        byte[] content = new byte[(int) backupFile.length()];
        try (FileInputStream fis = new FileInputStream(backupFile)) {
            fis.read(content);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_backup.ser");

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @PostMapping("/backup/import")
    public String importBackup(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "No file selected.");
            return "redirect:/backup";
        }

        try (ObjectInputStream ois = new ObjectInputStream(file.getInputStream())) {
            List<AppUser2> users = (List<AppUser2>) ois.readObject();
            appUserDAO2.saveUsers2(users);
            redirectAttributes.addFlashAttribute("message", "Backup imported successfully.");
        } catch (IOException | ClassNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to import backup: " + e.getMessage());
        }

        return "redirect:/backup";
    }
}
