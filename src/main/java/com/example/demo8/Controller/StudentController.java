package com.example.demo8.Controller;

import com.example.demo8.DAO.FileDAO;
import com.example.demo8.DAO.FileEntity;
import com.example.demo8.model.AppUser;
import com.example.demo8.DAO.AppUserDAOImpl;

import com.example.demo8.model.EditDto;

import com.example.demo8.model.HomeworkFile;
import com.example.demo8.model.SubmissionFile;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller

public class StudentController {

    @Autowired
    private AppUserDAOImpl appUserDAO;

    private static final String UPLOAD_DIR = "src\\main\\java\\com\\example\\demo8\\upload";



    private FileDAO fileDAO;



    @GetMapping("/downloadhomework")
    public String showDownloadAndUploadForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        AppUser user = appUserDAO.getUserByUsername(username);
        int id=user.getId();
        model.addAttribute("id", id);
        model.addAttribute("homeworks", appUserDAO.getAllHomeworkFiles());
        return "downloadhomework";
    }

    @PostMapping("/uploadsubmission")
    public String uploadSubmissionFile(@RequestParam("homeworkId") int homeworkId,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("userId") int userId, Model model ) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;
            try {
                File uploadFile = new File(filePath);
                file.transferTo(uploadFile);

                SubmissionFile submissionFile = new SubmissionFile(0, userId, homeworkId, fileName, filePath);
                appUserDAO.saveSubmissionFile(submissionFile);
                model.addAttribute("message", "File submitted successfully!");

            } catch (IOException e) {
                model.addAttribute("message", "File submission failed!");
                e.printStackTrace();
            }
        }
        return "downloadhomework";
    }

    @GetMapping("/downloadfile")
    public String downloadFile(@RequestParam("fileId") int fileId,Model model) {
        HomeworkFile homeworkFile = appUserDAO.getHomeworkFileById(fileId);

        model.addAttribute("fileLink", "/upload/" + homeworkFile.getFileName());

  return "downloadhomework";

    }
    @GetMapping("/user1")
    public String listUsers(Model model) {
        List<AppUser> users = appUserDAO.getAllUsers();
        model.addAttribute("user1", users);
        return "user1";
    }

    @GetMapping("/edit1/{id}")
    public String showEditForm1(@PathVariable("id") Integer id, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        AppUser user = appUserDAO.getUserById(id);
        if (user != null && username.equals(user.getUsername())) {
            EditDto editDto1 = new EditDto();
            editDto1.setId(String.valueOf(user.getId()));
            editDto1.setEmail(user.getEmail());
            editDto1.setSdt(user.getSdt());
            editDto1.setRole(user.getRole());
            model.addAttribute("editDto1", editDto1);
        } else {
            model.addAttribute("error", "User not found or you do not have permission to edit this user");
            return "error";
        }
        return "edit1";
    }

    @PostMapping("/edit1/{id}")
    public String updateAccount1(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute EditDto editDto1,
                                 BindingResult result,
                                 Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();

        AppUser user = appUserDAO.getUserById(id);
        if (user == null || !username.equals(user.getUsername())) {
            model.addAttribute("error", "User not found or you do not have permission to edit this user");
            return "error";
        }

        if (editDto1.getUsername() != null && !editDto1.getUsername().equals(user.getUsername())) {
            AppUser existingUser = appUserDAO.getUserByUsername(editDto1.getUsername());
            if (existingUser != null) {
                result.addError(new FieldError("editDto1", "username", "Username is already in use"));
            }
        }

        if (result.hasErrors()) {
            return "edit1";
        }

        try {

            user.setEmail(editDto1.getEmail());
            user.setSdt(editDto1.getSdt());
            user.setRole(editDto1.getRole());

            if (editDto1.getPassword() != null && !editDto1.getPassword().isEmpty()) {
                BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
                user.setPassword(bCryptEncoder.encode(editDto1.getPassword()));
            }

            appUserDAO.updateUser(user);
            model.addAttribute("success", true);
            model.addAttribute("editDto1", new EditDto());
        } catch (Exception e) {
            result.addError(new FieldError("editDto1", "username", e.getMessage()));
            return "edit1";
        }

        return "redirect:/user1";
    }
//    @GetMapping("/lambai/{id}")
//    public String showSubmitHomeworkForm(@PathVariable("id") int homeworkId, Model model) {
//        model.addAttribute("submissionFileDto", new SubmissionFileDto());
//        model.addAttribute("homeworkId", homeworkId);
//        return "lambai";
//    }
//    @PostMapping("/lambai/{id}")
//    public String submitHomework(@PathVariable("id") int homeworkId,
//                                 @RequestParam("file") MultipartFile file,
//                                 @RequestParam("studentId") int studentId,
//                                 Model model) {
//        String fileName = file.getOriginalFilename();
//        String filePath = "C:\\Users\\tuanlee\\IdeaProjects\\demo8\\src\\main\\java\\com\\example\\demo8\\upload\\bt" + fileName;
//
//        try {
//
//            File dest = new File(filePath);
//            file.transferTo(dest);
//
//
//            SubmissionFileDto submissionFileDto = new SubmissionFileDto();
//            submissionFileDto.setHomeworkId(homeworkId);
//            submissionFileDto.setStudentId(studentId);
//            submissionFileDto.setFileName(fileName);
//            submissionFileDto.setFilePath(filePath);
//
//            appUserDAO.saveSubmissionFile(submissionFileDto);
//
//            model.addAttribute("success", true);
//        } catch (IOException e) {
//            e.printStackTrace();
//            model.addAttribute("error", "File upload failed");
//        }
//
//        return "lambai";
//    }



}
