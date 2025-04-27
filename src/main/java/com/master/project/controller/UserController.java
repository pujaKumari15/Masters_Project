package com.master.project.controller;

import com.master.project.config.JwtResponse;
import com.master.project.config.JwtTokenProvider;
import com.master.project.model.User;
import com.master.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {

        if (user.getUserID() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username and Password are required");
        }

        try {
            User userDetails = userService.loginUser(user);

            if (userDetails != null) {
                String jwtToken = jwtTokenProvider.generateToken(String.valueOf(userDetails.getUserID()));
                JwtResponse jwtResponse = new JwtResponse(jwtToken);

                LOGGER.info("Jwt response generated for userID: " + userDetails.getUserID());
                return ResponseEntity.ok(jwtResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            LOGGER.error("Error during login: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing your request");
        }
    }
}
