package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private CloudUserService userService;

    public List<Note> findAllNotesByUserId(Integer userId) {
        return this.noteMapper.findAllNotesByUserId(userId);
    }

    public int upgradeNote(Note note) {
        if (note.getNoteId() == null) {
            return this.noteMapper.insertNote(note);
        } else {
            return this.noteMapper.updateNote(note);
        }
    }

    public int deleteNoteById(Integer noteId) {
        return this.noteMapper.deleteNoteById(noteId);
    }

}