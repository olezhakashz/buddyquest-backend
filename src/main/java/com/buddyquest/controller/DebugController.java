package com.buddyquest.controller;

import com.buddyquest.model.User;
import com.buddyquest.model.Quest;
import com.buddyquest.repository.UserRepository;
import com.buddyquest.repository.QuestRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final UserRepository userRepository;
    private final QuestRepository questRepository;

    public DebugController(UserRepository userRepository, QuestRepository questRepository) {
        this.userRepository = userRepository;
        this.questRepository = questRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/quests")
    public List<Quest> getQuests() {
        return questRepository.findAll();
    }
}
