package com.example.demo8.Controller;

import com.example.demo8.DAO.AppUserDAOImpl;
import com.example.demo8.model.Challenge;
import com.example.demo8.service.AudioConversionService;
import com.example.demo8.service.MediaConversionService;
import jakarta.servlet.http.HttpServletResponse;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.ffmpeg.avcodec.AVCodecContext;
//import org.bytedeco.ffmpeg.avcodec.AVFrame;
//import org.bytedeco.ffmpeg.avcodec.AVPacket;
//import org.bytedeco.ffmpeg.avformat.AVFormatContext;
//import org.bytedeco.ffmpeg.avformat.AVOutputFormat;
//import org.bytedeco.ffmpeg.avutil.AVFrame;
//import org.bytedeco.ffmpeg.avutil.AVCodec;
import org.bytedeco.ffmpeg.avutil.AVOption;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import javax.servlet.http.*;
@Controller
public class HomeController {


    private HttpServletRequest request;
    @Autowired
    private AppUserDAOImpl appUserDAO1;
    @Autowired
    private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    private static final String UPLOAD_DIR =  "\\upload";


    private static final String CHALLENGE_DIR =  "\\upload";



    @GetMapping("/create-challenge")
    public String showCreateChallengeForm() {
        return "create-challenge";
    }

    @PostMapping("/create-challenge")
    public String createChallenge(@RequestParam("file") MultipartFile file,
                                  @RequestParam("hint") String hint,
                                  Model model) {
        if (!file.isEmpty()) {
            try {

                String fileName = file.getOriginalFilename();
                String safeFileName = fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_").toLowerCase();
                Path filePath = Paths.get(CHALLENGE_DIR, safeFileName);


                Files.createDirectories(filePath.getParent());
                file.transferTo(filePath.toFile());


                appUserDAO1.saveChallenge(safeFileName, hint);

                model.addAttribute("message", "Challenge created successfully!");

                return "create-challenge";
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Error occurred while creating challenge.");
            }
        }
        return "create-challenge";
    }

    @GetMapping("/submit-answer")
    public String showSubmitAnswerForm(Model model) {
        List<Challenge> challenges = appUserDAO1.getAllChallenges();
        model.addAttribute("challenges", challenges);
        return "submit-answer";
    }

    @PostMapping("/submit-answer")
    public String submitAnswer(@RequestParam("challengeId") int challengeId,
                               @RequestParam("answer") String answer,
                               Model model) {
        Challenge challenge = appUserDAO1.getChallengeById(challengeId);
        answer=answer+".txt";
        if (challenge != null && challenge.getFileName().equalsIgnoreCase(answer.trim().replaceAll("[^a-zA-Z0-9\\.\\-]", "_"))) {
            try {
                Path filePath = Paths.get(CHALLENGE_DIR, challenge.getFileName());
                String fileContent = new String(Files.readAllBytes(filePath));
                model.addAttribute("fileContent", fileContent);
                model.addAttribute("message", "Correct! Here is the content:");
            } catch (IOException e) {
                model.addAttribute("message", "Error reading file.");
                e.printStackTrace();
            }
        } else {
            model.addAttribute("message", "Incorrect answer. Try again!");
        }

        List<Challenge> challenges = appUserDAO1.getAllChallenges();
        model.addAttribute("challenges", challenges);
        return "submit-answer";
    }

    @GetMapping({"", "/"})
    public String home() {
        return "index";
    }

    @GetMapping("/upload-url")
    public String showUploadPage() {
        return "upload-url";
    }
    private static String getFileExtension(String url) {

        String fileExtension = "";
        int lastDotIndex = url.lastIndexOf(".");
        if (lastDotIndex != -1 && lastDotIndex < url.length() - 1) {
            fileExtension = url.substring(lastDotIndex);
        }
        return fileExtension;
    }
    @PostMapping("/upload-url")
    public String uploadUrl(@RequestParam("url") String url, Model model) {
        System.out.println("1");
        String fileExtension = getFileExtension(url);
        String fileName = "downloadedFile" + fileExtension;
        if (!fileExtension.equals(".png") && !fileExtension.equals(".pdf") && !fileExtension.equals(".txt")) {
            fileExtension = ".txt";
        }
        String fileName1 = "downloadedFile" + fileExtension;
        try {





            URL fileUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream in = new BufferedInputStream(connection.getInputStream());

                 FileOutputStream out = new FileOutputStream(UPLOAD_DIR + fileName1)) {

                byte[] buffer = new byte[5242880];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }


            model.addAttribute("fileUrl",  "/upload/" +fileName1);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Có lỗi xảy ra khi tải file.");
        }

        return "preview";
    }


    public static class Resource {
        private String url;
        private String type;

        public Resource(String url, String type) {
            this.url = url;
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout=true";
    }
    private static final String UPLOADED_FOLDER = "\\upload";
    //private static final String FFMPEG_PATH = "C:\\Users\\tuanlee\\Downloads\\ffmpeg-master-latest-win64-gpl\\ffmpeg-master-latest-win64-gpl\\bin\\ffmpeg";
    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/upload";
        }

        try {

            File savedFile = new File(UPLOADED_FOLDER + file.getOriginalFilename());
            file.transferTo(savedFile);


            String mp4FilePath = savedFile.getAbsolutePath();
            String m4aFilePath = UPLOADED_FOLDER + file.getOriginalFilename().replace(".mp4", ".m4a");
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg","-y", "-i","-vn", "-acodec","copy" , mp4FilePath, m4aFilePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            System.out.println("tested");
            process.waitFor();


            redirectAttributes.addFlashAttribute("message", "File uploaded and converted successfully!");
            redirectAttributes.addFlashAttribute("fileLink", "/upload/" + file.getOriginalFilename());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to convert the file");
        }

        return "redirect:/upload";
    }

}