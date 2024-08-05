package com.example.demo8.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class PostLoginController {

    @GetMapping("/postLogin")
    public RedirectView handlePostLogin(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_0"))) {
            return new RedirectView("/users");
        } else {
            return new RedirectView("/user1");
        }
    }
}
