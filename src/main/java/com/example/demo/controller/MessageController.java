package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.repositories.MessageRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tenants/{tenantId}/messages")
public class MessageController {

    private MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping
    public void createMessage(@RequestBody Message message) {
        messageRepository.save(message);
    }
}
