package com.sarz.electronic.store.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.sarz.electronic.store.Security.JWTHelper;
import com.sarz.electronic.store.Service.UserService;
import com.sarz.electronic.store.dtos.JWTRequest;
import com.sarz.electronic.store.dtos.JWTResponse;
import com.sarz.electronic.store.dtos.UserDTO;
import com.sarz.electronic.store.entities.User;
import com.sarz.electronic.store.exceptions.BadApiRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Tag(name = "Auth Controller",description = "API's for Documentation!!")
@SecurityRequirement(name = "Scheme1")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private JWTHelper helper;
    @Autowired
    private UserService userService;
    @Value("${googleClientId}")
    private String googleClientId;

    @Value("${newPassword}")
    private String newPassword;
    Logger logger = LoggerFactory.getLogger(AuthController.class);
    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(mapper.map(userDetailsService.loadUserByUsername(name),UserDTO.class), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request){
        this.doAuthenticate(request.getEmail(),request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        UserDTO userDTO = mapper.map(userDetails, UserDTO.class);
        JWTResponse response = JWTResponse.builder().jwtToken(token).user(userDTO).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    public void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(email,password);
        try{
            authenticationManager.authenticate(authentication);
        }catch (BadCredentialsException e){
            throw new BadApiRequest("Invalid Username and password !!");
        }
    }

    // LOGIN WITH GOOGLE API

    @PostMapping("/google")
    public ResponseEntity<JWTResponse> loginWithGoogle(@RequestBody Map<String,Object> data) throws IOException {

        //GET THE ID TOKEN FROM THE REQUEST
        String idToken=data.get("idToken").toString();
        NetHttpTransport netHttpTransport = new NetHttpTransport();
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
        GoogleIdToken googleIdToken= GoogleIdToken.parse(verifier.getJsonFactory(),idToken);
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        logger.info("payload : {}",payload);
        String email = payload.getEmail();

        User user = null;
        user=userService.findUserByEmailOptional(email).orElse(null);
        if(user==null){
            //create a new user
            user=this.saveUser(email,data.get("name").toString(),data.get("photoUrl").toString());
        }
        ResponseEntity<JWTResponse> jwtResponseResponseEntity = this.login(JWTRequest.builder().email(user.getEmail()).password(newPassword).build());
        return jwtResponseResponseEntity;
    }

    private User saveUser(String email, String name, String photoUrl) {

        UserDTO newUser = UserDTO.builder().name(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();

        UserDTO userDTO = userService.CreateUser(newUser);
        return mapper.map(userDTO,User.class);
    }
}
