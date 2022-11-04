package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.repo.Repo;

@Controller
public class TestController {
    private final Repo repo;

    public TestController(Repo repo) {
        this.repo = repo;
    }

    @GetMapping(path = "/test")
    ResponseEntity<?> getTests() {
        return ResponseEntity.ok(repo.findAll());
    }
}
