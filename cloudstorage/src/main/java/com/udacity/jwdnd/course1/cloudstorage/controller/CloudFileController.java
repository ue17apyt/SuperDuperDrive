package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import com.udacity.jwdnd.course1.cloudstorage.model.CloudUser;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudFileService;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class CloudFileController {

    @Autowired
    private CloudFileService fileService;

    @Autowired
    private CloudUserService userService;

    @PostMapping("/uploadFile")
    public String uploadFile(
            @RequestParam("fileUpload") MultipartFile file, Authentication authentication, RedirectAttributes attributes
    ) {

        attributes.addFlashAttribute("activeTab", "files");

        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", true);
            attributes.addFlashAttribute("message", "Empty File.\n");
            return "redirect:/result";
        }

        CloudUser user = this.userService.findUserByUsername(authentication.getName());

        if (!this.fileService.isFilenameAvailable(user.getUserId(), file.getOriginalFilename())) {
            attributes.addFlashAttribute("error", true);
            attributes.addFlashAttribute("message", "Duplicate Filename.\n");
            return "redirect:/result";
        }

        try {
            this.fileService.insertFile(file, user.getUserId());
            attributes.addFlashAttribute("success", true);
            attributes.addFlashAttribute("message", "Successful File Upload.\n");
        } catch (IOException ioException) {
            attributes.addFlashAttribute("error", true);
            attributes.addFlashAttribute("message", "Failed Temporary Storage.\n");
        }
        return "redirect:/result";

    }

    @RequestMapping("/viewFile")
    public ResponseEntity<Resource> viewFile(@RequestParam("fileId") Integer fileId, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "files");
        CloudFile file = this.fileService.findFileById(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new InputStreamResource(file.getFileData()));
    }

    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam String fileId, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "files");
        this.fileService.deleteFileById(Integer.parseInt(fileId));
        attributes.addFlashAttribute("success", true);
        return "redirect:/result";
    }

}