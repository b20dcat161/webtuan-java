package com.example.demo8.Controller;

import com.example.demo8.model.*;
import com.example.demo8.DAO.AppUserDAOImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller

public class TeacherController {
    @Autowired
    private AppUserDAOImpl appUserDAO;
    private static final String UPLOAD_DIR = "src\\main\\java\\com\\example\\demo8\\upload";
    @GetMapping("/users")

    public String listUsers(Model model) {
        List<AppUser> users = appUserDAO.getAllUsers();
        model.addAttribute("userlist", users);
        return "userlist";
    }

    @GetMapping("/giao/{id}")
    public String viewStudentSubmissions(@PathVariable("id") int studentId, Model model) {
        List<SubmissionFile> submissions = appUserDAO.getSubmissionsByStudentId(studentId);
        System.out.println(studentId);
        model.addAttribute("submissions", submissions);
        model.addAttribute("studentId", studentId); // Thêm ID sinh viên vào mô hình để sử dụng trong view

        return "giao";
    }
    @GetMapping("/uploadhomework")
    public String showUploadForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        AppUser user = appUserDAO.getUserByUsername(username);
        int id=user.getId();
        model.addAttribute("id", id);
        return "uploadhomework";
    }

    @PostMapping("/uploadhomework")
    public String uploadHomeworkFile(MultipartFile file, int userId, Model model) {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;
            try {
                File uploadFile = new File(filePath);
                file.transferTo(uploadFile);

                HomeworkFile homeworkFile = new HomeworkFile(0, userId, fileName, filePath);
                appUserDAO.saveHomeworkFile(homeworkFile);
                model.addAttribute("message", "File uploaded successfully!");

            } catch (IOException e) {
                model.addAttribute("message", "File upload failed!");
                e.printStackTrace();
            }
        }
        return "uploadhomework";
    }

    private String generateRandomFileName(String originalFileName) {
        String uniqueID = UUID.randomUUID().toString();
        String fileExtension = ".txt"; // We want all files to be saved with .txt extension
        return uniqueID + fileExtension;
    }

    @GetMapping("/detail/{id}")
    public String userDetails(@PathVariable("id") int id,  Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        AppUser user = appUserDAO.getUserByUsername(username);
        int SenderId=user.getId();
        List<Message> messages = appUserDAO.getMessagesBetween(SenderId, id);
        messages.sort(Comparator.comparingInt(Message::getId));

        AppUser user1 = appUserDAO.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user1);

   model.addAttribute("messages", messages);
  model.addAttribute("otherUserId", id);

            return "userdetail";
        } else {
            return "redirect:/users";
        }
    }

    @PostMapping("/detail/{id}/sendMessage")
    public String sendMessage(@PathVariable("id") int id, @RequestParam String messageContent) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        AppUser user = appUserDAO.getUserByUsername(username);
        int SenderId=user.getId();
        Message message = new Message();
        message.setSenderId(SenderId);

        message.setReceiverId(id);
        message.setMessage(messageContent);
        message.setSentAt(new java.util.Date());


        appUserDAO.sendMessage(SenderId, id, messageContent);


        return "redirect:/detail/" + id ;
    }


    @PostMapping("/detail/{id}/updateMessage")
    public String updateMessage(@PathVariable("id") int id, @RequestParam int messageId, @RequestParam String newContent, @RequestParam int receiverId) {

        Message message = new Message();
        message.setId(messageId);
        message.setSenderId(id);
        message.setMessage(newContent);
        message.setSentAt(new java.util.Date());


        appUserDAO.updateMessage(messageId, newContent);


        return "redirect:/detail/" + id + "?otherUserId=" + receiverId;
    }


    @PostMapping("/detail/{id}/deleteMessage")
    public String deleteMessage(@PathVariable("id") int id, @RequestParam int messageId, @RequestParam int receiverId) {

        appUserDAO.deleteMessage(messageId);


        return "redirect:/detail/" + id + "?otherUserId=" + receiverId;
    }


    @GetMapping("/register")
    public String register(@RequestParam(value = "id", required = false) Integer id,
                           @RequestParam(value = "action", required = false) String action,
                           Model model) {
        RegisterDto registerDto = new RegisterDto();

        if (id != null && "edit".equals(action)) {
            AppUser user = appUserDAO.getUserById(id);
            if (user != null) {
                registerDto.setName(user.getName());
                registerDto.setEmail(user.getEmail());
                registerDto.setSdt(user.getSdt());
                registerDto.setUsername(user.getUsername());
                registerDto.setRole(user.getRole());

            }
        }

        model.addAttribute("registerDto", registerDto);
        model.addAttribute("success", false);
        model.addAttribute("editMode", "edit".equals(action)); // Flag to differentiate between add and edit mode

        return "register";
    }


    @PostMapping("/register")
    public String register(Model model,
                           @Valid @ModelAttribute RegisterDto registerDto,
                           BindingResult result,
                           @RequestParam(value = "id", required = false) Integer id) {

        if (registerDto.getUsername() != null) {
            AppUser existingUser = appUserDAO.getUserByUsername(registerDto.getUsername());
            if (existingUser != null && (id == null || existingUser.getId() != id)) {
                result.addError(new FieldError("registerDto", "username", "Username is already in use"));
            }
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
            AppUser user = id != null ? appUserDAO.getUserById(id) : new AppUser();

            if (user == null) {
                user = new AppUser();
            }

            user.setName(registerDto.getName());
            user.setEmail(registerDto.getEmail());
            user.setSdt(registerDto.getSdt());
            user.setUsername(registerDto.getUsername());
            if (registerDto.getPassword() != null && !registerDto.getPassword().isEmpty()) {
                user.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
            }
            user.setRole(registerDto.getRole());

            if (id != null) appUserDAO.updateUser(user);
            else {
                appUserDAO.saveUser(user);
            }

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);
        } catch (Exception e) {
            result.addError(new FieldError("registerDto", "username", e.getMessage()));
            return "register";
        }

        return "register";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") int id, Model model) {
        try {
            appUserDAO.deleteUser(id);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", e.getMessage());
        }


        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        AppUser user = appUserDAO.getUserById(id);
        if (user != null) {
            EditDto editDto = new EditDto();
            editDto.setId(String.valueOf(user.getId()));
            editDto.setName(user.getName());
            editDto.setEmail(user.getEmail());
            editDto.setSdt(user.getSdt());
            editDto.setUsername(user.getUsername());
            editDto.setRole(user.getRole());
            model.addAttribute("editDto", editDto);
        } else {
            model.addAttribute("error", "User not found");
        }
        return "edit";
    }
    @PostMapping("/edit/{id}")
    public String updateAccount(@PathVariable("id") Integer id,
                                @Valid @ModelAttribute EditDto editDto,
                                BindingResult result,
                                Model model) {
        System.out.println("111111111111111111111111111111111111");

        if (editDto.getUsername() != null) {
            AppUser existingUser = appUserDAO.getUserByUsername(editDto.getUsername());
            if (existingUser != null && existingUser.getId() != id) {
                result.addError(new FieldError("editDto", "username", "Username is already in use"));
            }
        }

        if (result.hasErrors()) {
            return "edit";
        }

        try {
            BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
            AppUser user = appUserDAO.getUserById(id);
            if (user == null) {
                model.addAttribute("error", "User not found");
                return "edit";
            }

            user.setName(editDto.getName());
            user.setEmail(editDto.getEmail());
            user.setSdt(editDto.getSdt());
            user.setUsername(editDto.getUsername());
            if (editDto.getPassword() != null && !editDto.getPassword().isEmpty()) {
                user.setPassword(bCryptEncoder.encode(editDto.getPassword()));
            }
            user.setRole(editDto.getRole());

            appUserDAO.updateUser(user);

            model.addAttribute("success", true);
            model.addAttribute("editDto", new EditDto());

        } catch (Exception e) {
            result.addError(new FieldError("editDto", "username", e.getMessage()));
            return "edit";
        }

        return "/edit";
    }
//
//    @Autowired
//    private HomeworkFileService homeworkFileService;
//
//    @GetMapping("/assign/{studentId}")
//    public String showAssignHomeworkForm(@PathVariable("studentId") Integer studentId, Model model) {
//        model.addAttribute("homeworkFile", new HomeworkFile());
//        model.addAttribute("studentId", studentId);
//        return "assign-homework";
//    }
//
//    @PostMapping("/assign/{studentId}")
//    public String assignHomework(@PathVariable("studentId") Integer studentId, @ModelAttribute HomeworkFile homeworkFile) {
//        homeworkFile.setStudentId(studentId); // Associate homework with the student
//        homeworkFileService.uploadHomeworkFile(homeworkFile);
//        return "redirect:/homework/assigned/{studentId}";
//    }
//    @GetMapping("/giao/{id}")
//    public String showUploadHomeworkForm(@PathVariable("id") int teacherId, Model model) {
//        model.addAttribute("homeworkFileDto", new HomeworkFileDto());
//        model.addAttribute("teacherId", teacherId);
//        return "giao";
//    }
//
//    @PostMapping("/giao/{id}")
//    public String uploadHomework(@PathVariable("id") int teacherId,
//                                 @RequestParam("file") MultipartFile file,
//                                 @RequestParam(value = "studentId", required = false) Integer studentId,
//                                 Model model) {
//        String fileName = file.getOriginalFilename();
//        String filePath = "C:\\Users\\tuanlee\\IdeaProjects\\demo8\\src\\main\\java\\com\\example\\demo8\\upload\\bt" + fileName; // Adjust path as needed
//
//        try {
//
//            File dest = new File(filePath);
//            file.transferTo(dest);
//
//
//            HomeworkFileDto homeworkFileDto = new HomeworkFileDto();
//            homeworkFileDto.setTeacherId(teacherId);
//            homeworkFileDto.setFileName(fileName);
//            homeworkFileDto.setFilePath(filePath);
//            homeworkFileDto.setStudentId(studentId);
//
//            appUserDAO.saveHomeworkFile(homeworkFileDto);
//
//            model.addAttribute("success", true);
//        } catch (IOException e) {
//            e.printStackTrace();
//            model.addAttribute("error", "File upload failed");
//        }
//
//        return "giao";
//    }


}
