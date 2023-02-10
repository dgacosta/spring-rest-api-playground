package com.playground.application;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<UserModel> findById(@PathVariable Long requestedId) {
        Optional<UserModel> userOptional = userRepository.findById(requestedId);
            if (!userOptional.isEmpty()) {
                return ResponseEntity.ok(userOptional.get());
            } else {
                return ResponseEntity.notFound().build();
            }
    }
}
