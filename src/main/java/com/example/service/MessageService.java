package com.example.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message postMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(long message_id) {
        messageRepository.findById(message_id).get();
        messageRepository.deleteById(message_id);
    }

    public void updateMessage(long message_id, String new_message) {
        Message message = messageRepository.findById(message_id).get();
        message.setMessageText(new_message);
        messageRepository.save(message);
    }

    public Message getMessage(long message_id) {
        return messageRepository.findById(message_id).get();
    }

    public List<Message> getUserMessages(int posted_by) {
        return messageRepository.findByPostedBy(posted_by);
    }
}
