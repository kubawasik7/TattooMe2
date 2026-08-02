package service.UserService;

import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceGetTopArtistsTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;
    @Test
    public void shouldReturnTopArtists() {
        UserDTO artist1 = new UserDTO(
                UUID.randomUUID(),
                "janek",
                "Jan",
                "Kowalski",
                "jan@test.pl",
                "Opis artysty",
                null,
                4.8,
                20L,
                List.of()
        );

        UserDTO artist2 = new UserDTO(
                UUID.randomUUID(),
                "marek",
                "Marek",
                "Nowak",
                "marek@test.pl",
                "Opis",
                null,
                4.5,
                15L,
                List.of()
        );


        List<UserDTO> artists =
                List.of(artist1, artist2);

        when(userRepository.findTopUsersWithAvgRating(any(Pageable.class)))
                .thenReturn(artists);

        List<UserDTO> result =
                userService.getTop5Artists();

        assertEquals(2, result.size());
        assertEquals(artists, result);

        verify(userRepository)
                .findTopUsersWithAvgRating(any(Pageable.class));
    }
}
