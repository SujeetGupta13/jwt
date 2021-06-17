package com.jwtDemo.JWTDemo.controller;

import com.jwtDemo.JWTDemo.model.JwtRequest;
import com.jwtDemo.JWTDemo.model.JwtResponse;
import com.jwtDemo.JWTDemo.service.UserService;
import com.jwtDemo.JWTDemo.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(){
        return "Welcome";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {

        System.out.println("hello");
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           jwtRequest.getUserName(),
                           jwtRequest.getPassword()
                   )
           );
       } catch (BadCredentialsException e) {
           throw new Exception("INVALID_CREDENTIALS", e);
       }

       final UserDetails userDetails =  userService.loadUserByUsername(
               jwtRequest.getUserName());
       final  String token = jwtUtility.generateToken(userDetails);

       return new JwtResponse(token);
    }


}
