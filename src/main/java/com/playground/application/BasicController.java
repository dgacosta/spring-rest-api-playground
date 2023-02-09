package com.playground.application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/")
    public String home() {
        return "basic home";
    }

    @GetMapping("/{requestedText}")
    public ResponseEntity<String> findById(@PathVariable String requestedText) {
        return ResponseEntity.ok(requestedText);
    }
}
