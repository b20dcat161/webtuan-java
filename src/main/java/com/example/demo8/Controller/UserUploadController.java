package com.example.demo8.Controller;

import com.example.demo8.DAO.AppUserDAOImpl;
import com.example.demo8.model.AppUser1;

//import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.example.demo8.model.AppUser;
import com.example.demo8.model.UserListDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.bytedeco.opencv.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class UserUploadController {

    @Autowired
    private AppUserDAOImpl appUserDAO;
    @Autowired
    private DataSourceTransactionManagerAutoConfiguration dataSourceTransactionManagerAutoConfiguration;

    @GetMapping("/abc")
    public String uploadForm(Model model) {

        model.addAttribute("users");
        return "mp4";
    }

    @PostMapping("/abc")
    public ModelAndView handleFileUpload(@RequestParam("xmlFile") MultipartFile file,WebRequest request,Model model) throws IOException {
        ModelAndView mav = new ModelAndView();
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);

        // Hiển thị nội dung lên trang
        model.addAttribute("fileContent", content);
        if (file.isEmpty()) {
            mav.addObject("errorMessage", "No file selected.");
            mav.setViewName("usersManager"); // Forward to usersManager.jsp
            return mav;
        }

        try (InputStream fileContent = file.getInputStream()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(fileContent);

            List<AppUser1> users = handleXmlUsers(doc);
            mav.addObject("users", users);
            model.addAttribute("file", file);
            for( AppUser1 u : users) {
                System.out.println(u);
            }
            mav.setViewName("mp4");

        } catch (ParserConfigurationException | SAXException | IOException e) {

            mav.setViewName("redirect:/users");
        }

        return mav;
    }


    private List<AppUser1> handleXmlUsers(Document xmlDocument) {

        List<AppUser1> users = new ArrayList<>();
        NodeList userNodes = xmlDocument.getElementsByTagName("user");
        for (int i = 0; i < userNodes.getLength(); i++) {
            Node userNode = userNodes.item(i);
            if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) userNode;
                String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                String fullname = userElement.getElementsByTagName("fullname").item(0).getTextContent();
                String role = userElement.getElementsByTagName("role").item(0).getTextContent();
                String email = userElement.getElementsByTagName("email").item(0).getTextContent();
                String phone = userElement.getElementsByTagName("phone").item(0).getTextContent();
                String password = "123456a@A";
                users.add(new AppUser1(username, email, phone, fullname, role, password));
            }
        }
        return users;
    }

    @PostMapping("/saveUsers")
    public ModelAndView handleFileUpload1(@RequestParam("xmlFile") MultipartFile file,WebRequest request,Model model) throws IOException {
        ModelAndView mav = new ModelAndView();

        if (file.isEmpty()) {
            mav.addObject("errorMessage", "No file selected.");
            mav.setViewName("usersManager"); // Forward to usersManager.jsp
            return mav;
        }

        try (InputStream fileContent = file.getInputStream()) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(fileContent);

            List<AppUser1> users = handleXmlUsers(doc);

            for( AppUser1 u : users) {
               appUserDAO.saveUser2(u);
            }
            mav.setViewName("redirect:/users");

        } catch (ParserConfigurationException | SAXException | IOException e) {

            mav.setViewName("redirect:/users");
        }

        return mav;
    }


}

