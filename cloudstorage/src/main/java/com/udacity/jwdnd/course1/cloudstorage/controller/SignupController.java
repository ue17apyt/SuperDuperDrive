package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudUser;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private CloudUserService userService;

    @GetMapping()
    public String signupView() {
        return "signup";
    }

    @PostMapping()
    public String signupUser(@ModelAttribute CloudUser user, Model model) {

        String signupError = null;

        if (!this.userService.isUsernameAvailable(user.getUsername())) {
            signupError = "The username has already existed.";
        }

        if (signupError == null) {
            int addedRow = this.userService.createUser(user);
            if (addedRow < 0) {
                signupError = "There was an error when you sign up.\nPlease try again!";
            }
        }

        if (signupError == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signupError);
        }

        return "signup";

    }

}