package com.example.student_registration_system.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BlacklistService {
    private final Set<String> blacklist = new HashSet<>();

    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
