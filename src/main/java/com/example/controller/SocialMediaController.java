package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Account account) {
        boolean isExistingUser = false;
        List<Account> accounts = accountService.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUsername().equals(account.getUsername())) {
                isExistingUser = true;
            }
        }
        if (!isExistingUser) {
            if (!account.getUsername().isBlank() && account.getPassword().length() >= 4) {
                Account newAccount = accountService.addAccount(account);
                return ResponseEntity.status(200).body(newAccount);
            } else {
                return ResponseEntity.status(400).body("");
            }
        } else {
            return ResponseEntity.status(409).body("");
        }
    }

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        boolean isExistingUser = false;
        Account validAccount = null;
        List<Account> accounts = accountService.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUsername().equals(account.getUsername())) {
                isExistingUser = true;
                validAccount = accounts.get(i);
            }
        }
        if (isExistingUser) {
            return ResponseEntity.status(200).body(validAccount);
        } else {
            return ResponseEntity.status(401).body("");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity postMessage(@RequestBody Message message) {
        boolean isValidUser = false;
        List<Account> accounts = accountService.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountId().equals(message.getPostedBy())) {
                isValidUser = true;
            }
        }
        if (isValidUser && !message.getMessageText().isBlank() && message.getMessageText().length() <= 255) {
            Message newMessage = messageService.postMessage(message);
            return ResponseEntity.status(200).body(newMessage);
        } else {
            return ResponseEntity.status(400).body("");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity getMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity getMessageById(@PathVariable long messageId) {
        Message message = messageService.getMessage(messageId);
        return ResponseEntity.status(200).body(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity deleteMessageById(@PathVariable long messageId) {
        boolean isExistingMessage = false;
        List<Message> messages = messageService.getAllMessages();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageId().equals(messageId)) {
                isExistingMessage = true;
            }
        }
        if (isExistingMessage) {
            messageService.deleteMessage(messageId);
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).body("");
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity updateMessageById(@PathVariable long messageId, @RequestBody String newMessageText) {
        boolean isExistingMessage = false;
        List<Message> messages = messageService.getAllMessages();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageId().equals(messageId)) {
                isExistingMessage = true;
            }
        }
        if (isExistingMessage && !newMessageText.isBlank() && newMessageText.length() <= 255) {
            messageService.updateMessage(messageId, newMessageText);
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(400).body("");
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity updateMessageById(@PathVariable int accountId) {
        List<Message> userMessages = messageService.getUserMessages(accountId);
        return ResponseEntity.status(400).body(userMessages);
    }
}
