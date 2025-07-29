package com.planwise.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planwise.model.ContactMessage;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactMessageService {

    private static final String FILE_PATH = "data/contact_messages.json";

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Save the message by appending it to the existing list in file
    public void saveMessage(ContactMessage message) throws IOException {
        List<ContactMessage> messages = new ArrayList<>();

        File file = new File(FILE_PATH);
        if (file.exists()) {
            // Read existing messages
            ContactMessage[] existing = objectMapper.readValue(file, ContactMessage[].class);
            for (ContactMessage m : existing) {
                messages.add(m);
            }
        } else {
            file.getParentFile().mkdirs();
        }

        messages.add(message);

        // Write updated list back to file
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, messages);
    }
}