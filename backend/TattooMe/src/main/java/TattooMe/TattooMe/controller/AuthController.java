package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.dto.LoginRequest;
import TattooMe.TattooMe.dto.LoginResponse;
import TattooMe.TattooMe.dto.RegisterRequest;
import TattooMe.TattooMe.dto.RegisterResponse;
import TattooMe.TattooMe.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = userService.registerUser(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = userService.loginUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
