package com.major.identityservice.controller;


import com.major.identityservice.ErrorHandling.CustomException;
import com.major.identityservice.dto.AuthRequest;
import com.major.identityservice.dto.CustomResponse;
import com.major.identityservice.entity.UserCredential;
import com.major.identityservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<CustomResponse> addNewUser(@RequestBody UserCredential user) {
        service.saveUser(user);
        CustomResponse response = new CustomResponse(HttpStatus.OK.value(), "Registration successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/token")
    public ResponseEntity<CustomResponse> getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            CustomResponse response = new CustomResponse(HttpStatus.OK.value(), service.generateToken(authRequest.getUsername()));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            throw new CustomException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }
}
