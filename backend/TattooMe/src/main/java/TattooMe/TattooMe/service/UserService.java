package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.RegisterRequest;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    public void registerUser(RegisterRequest registerRequest){
        if(userRepository.findByNickname(registerRequest.getNickname()).isPresent()){
            throw new RuntimeException("Username is already taken");
        }

        User user = new User();
        user.setRole(registerRequest.getRole());
        user.setNickname(registerRequest.getNickname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);
    }
    public User getByNickname(String nickname){
        //tylko return potrzebny, reszta jest do testow
        Optional<User> user = userRepository.findByNickname(nickname);
        if(user.isPresent()){
            User user1 = user.get();
            System.out.println("id " + user1.getId());
            System.out.println("nickname " + user1.getNickname());
            System.out.println("email " + user1.getEmail());
            System.out.println("user role " + user1.getRole());
        }
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }
    public User getUserById(String id){
        Optional<User> user = userRepository.findById(UUID.fromString(id));
        if(user.isPresent()){
            return user.get();
        }else{
            throw new RuntimeException("user not found");
        }

    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}
