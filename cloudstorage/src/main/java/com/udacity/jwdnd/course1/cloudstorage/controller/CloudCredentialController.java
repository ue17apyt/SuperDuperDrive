package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudCredential;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CloudCredentialController {

    @Autowired
    private CloudCredentialService credentialService;

    @PostMapping("/upgradeCredential")
    public String upgradeCredential(
            @ModelAttribute("credentialForm") CloudCredential credential,
            Authentication authentication,
            RedirectAttributes attributes
    ) {

        attributes.addFlashAttribute("activeTab", "credentials");
        int success = this.credentialService.upgradeCredential(credential, authentication);

        if (success > 0) {
            attributes.addFlashAttribute("success", true);
            attributes.addFlashAttribute("message", "Successful Credential Upgrade.\n");
        } else {
            attributes.addFlashAttribute("error", true);
            attributes.addFlashAttribute("message", "Failed Credential Upgrade.\n");
        }

        return "redirect:/result";

    }

    @GetMapping("/deleteCredential")
    public String deleteNote(@RequestParam("credentialId") Integer credentialId, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "credentials");
        this.credentialService.deleteCredentialById(credentialId);
        attributes.addFlashAttribute("success", true);
        attributes.addFlashAttribute("message", "Successful Credential Deletion.\n");
        return "redirect:/result";
    }

}