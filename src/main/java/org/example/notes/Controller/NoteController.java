package org.example.notes.Controller;

import org.example.notes.Entity.Note;
import org.example.notes.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        return noteRepository.save(note);
    }

    @GetMapping("/view")
    public String viewNotes(Model model) {
        List<Note> notes = noteRepository.findAll();
        model.addAttribute("noteList", notes); // "noteList" ist der Name des Attributs
        return "index"; // Dies wird auf die index.html-Vorlage verweisen
    }

    @GetMapping("/edit/{id}")
    public String editNoteForm(@PathVariable Long id, Model model) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Notiz-ID: " + id));
        model.addAttribute("note", note);
        return "edit"; // Rückgabe der edit.html-Seite
    }

    @PostMapping("/edit/{id}")
    public String editNote(@PathVariable Long id, @ModelAttribute Note updatedNote) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ungültige Notiz-ID: " + id));
        note.setTitle(updatedNote.getTitle());
        note.setContent(updatedNote.getContent());
        noteRepository.save(note);
        return "redirect:/api/notes/view";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        noteRepository.deleteById(id);
        return "redirect:/api/notes/view";
    }

    // Weitere Endpunkte für das Aktualisieren, Löschen und Abrufen von einzelnen Notizen
}

