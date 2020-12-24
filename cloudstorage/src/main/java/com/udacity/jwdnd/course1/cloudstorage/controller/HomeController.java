package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudCredential;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudUser;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudCredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudFileService;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudUserService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private CloudFileService filesService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CloudCredentialService credentialService;

    @Autowired
    private CloudUserService userService;

    @Autowired
    private EncryptionService encryptionService;

    @GetMapping()
    public String homeView(
            @ModelAttribute("noteForm") Note note,
            @ModelAttribute("credentialForm") CloudCredential credential,
            Authentication authentication,
            Model model
    ) {

        CloudUser user = this.userService.findUserByUsername(authentication.getName());

        model.addAttribute("fileList", this.filesService.findAllFilesByUserId(user.getUserId()));
        model.addAttribute("noteList", this.noteService.findAllNotesByUserId(user.getUserId()));
        model.addAttribute(
                "credentialList", this.credentialService.findAllCredentialsByUserId(user.getUserId())
        );
        model.addAttribute("encryptionService", this.encryptionService);
        model.addAttribute("activeTab", "files");

        return "home";

    }

}