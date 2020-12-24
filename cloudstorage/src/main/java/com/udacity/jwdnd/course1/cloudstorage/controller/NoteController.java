package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudUser;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CloudUserService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private CloudUserService userService;

    @PostMapping("/upgradeNote")
    public String upgradeNote(
            @ModelAttribute("noteForm") Note note, Authentication authentication, RedirectAttributes attributes
    ) {

        attributes.addFlashAttribute("activeTab", "notes");
        CloudUser user = this.userService.findUserByUsername(authentication.getName());
        note.setUserId(user.getUserId());

        int success = this.noteService.upgradeNote(note);
        if (success > 0) {
            attributes.addFlashAttribute("success", true);
            attributes.addFlashAttribute("message", "Successful Note Upgrade.\n");
        } else {
            attributes.addFlashAttribute("error", true);
            attributes.addFlashAttribute("message", "Failed Note Upgrade.\n");
        }

        return "redirect:/result";

    }

    @GetMapping("/deleteNote")
    public String deleteNote(@RequestParam Integer noteId, RedirectAttributes attributes) {
        attributes.addFlashAttribute("activeTab", "notes");
        this.noteService.deleteNoteById(noteId);
        attributes.addFlashAttribute("success", true);
        attributes.addFlashAttribute("message", "Successful Note Deletion.\n");
        return "redirect:/result";
    }

}