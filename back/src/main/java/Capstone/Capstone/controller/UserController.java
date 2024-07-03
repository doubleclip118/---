package Capstone.Capstone.controller;

import Capstone.Capstone.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<UserDTO> UserRegister(@Valid @RequestBody ){

    }

}
