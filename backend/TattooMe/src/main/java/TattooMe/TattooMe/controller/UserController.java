package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> getAllUsers(){
        return userService.findAllUsers();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(String.valueOf(id)));
    }
    @GetMapping("/tattooArtist")
    public List<User> getAllTattooArtist(){
        return userService.findAllTattooArtist();
    }
    @GetMapping("/trainee")
    public List<User> getAllTrainee(){
        return userService.findAllTrainees();
    }
}
