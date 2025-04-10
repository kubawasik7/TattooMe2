package TattooMe.TattooMe.service;

import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}
