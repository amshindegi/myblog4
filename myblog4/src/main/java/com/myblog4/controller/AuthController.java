package com.myblog4.controller;

import com.myblog4.entity.User;
import com.myblog4.payload.JWTAuthResponse;
import com.myblog4.payload.LoginDto;
import com.myblog4.payload.SignUpDto;
import com.myblog4.repository.UserRepository;
import com.myblog4.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            new ResponseEntity<>("Email is already exist "+signUpDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            new ResponseEntity<>("Username is already "+signUpDto.getUsername(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user= new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setUser_roll(signUpDto.getUser_roll());

        userRepository.save(user);
        return new ResponseEntity<>("User is now Registered.", HttpStatus.CREATED);
    }
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

}
