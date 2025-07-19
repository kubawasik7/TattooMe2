package TattooMe.TattooMe.Security;

import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CustomUserDetailService implements  UserDetailsService{
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByNickname(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Nie znaleziono użytkownika o loginie: " + username)
                );

        return new CustomUserDetails(user);
    }

    public CustomUserDetails loadUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Nie znaleziono użytkownika o ID: " + id)
                );
        return new CustomUserDetails(user);
    }
}
