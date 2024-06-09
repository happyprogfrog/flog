package me.progfrog.flog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserViewController {

    @GetMapping("/login")
    public String getLoginForm() {
        return "users/oauthLogin";
    }

    @GetMapping("/signup")
    public String getSignupForm() {
        return "users/signup";
    }
}
