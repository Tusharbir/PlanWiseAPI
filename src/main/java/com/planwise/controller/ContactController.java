package com.planwise.controller;

import com.planwise.model.ContactMessage;
import com.planwise.service.ContactMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactMessageService contactService;

    public ContactController(ContactMessageService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> receiveMessage(@RequestBody ContactMessage message) {
        // You can add server-side validation here if needed

        try {
            contactService.saveMessage(message);
            return ResponseEntity.ok().body("{\"status\": \"Message received successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"Failed to save message\"}");
        }
    }
}