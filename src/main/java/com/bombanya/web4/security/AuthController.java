package com.bombanya.web4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    @Autowired
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('API_USER')")
    ResponseEntity<?> auth(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signUp")
    ResponseEntity<String> signUp(@RequestBody NewUser newUser){
        if (!userRepository.existsByUsername(newUser.getUsername())){
            if (newUser.getUsername() == null || newUser.getUsername().length() == 0 ||
                    newUser.getUsername().length() > 50){
                return new ResponseEntity<>("Login cannot be an empty string or longer than 50 chars"
                        , HttpStatus.NOT_ACCEPTABLE);
            }
            System.out.println(newUser.getPassword());
            User user = new User();
            user.setUsername(newUser.getUsername());
            user.setPassword(newUser.getPassword());
            userRepository.save(user);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        else return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
    }
}
