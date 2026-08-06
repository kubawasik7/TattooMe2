package controller;

import TattooMe.TattooMe.Security.JwtAuthenticationFilter;
import TattooMe.TattooMe.Security.JwtUtil;
import TattooMe.TattooMe.TattooMeApplication;
import TattooMe.TattooMe.controller.UserController;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = TattooMeApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    JwtUtil jwtUtil;
    @MockitoBean
    JwtAuthenticationFilter  jwtAuthenticationFilter;

    @Test
    void shouldReturnUserById() throws Exception {
        UUID userId = UUID.randomUUID();

        UserDTO userDTO = new UserDTO(
                userId,
                "jan",
                "Jan",
                "Kowalski",
                "jan@test.pl",
                "Opis",
                null,
                4.8,
                10L,
                List.of()
        );

        when(userService.getUserByIdWithAvgRating(userId))
                .thenReturn(Optional.of(userDTO));

        mockMvc.perform(
                get("/api/users/{id}", userId)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname")
                        .value("jan"));

        verify(userService).getUserByIdWithAvgRating(userId);
    }

    @Test
    void shouldReturnAllArtistsWithRating() throws Exception {
        UUID userId = UUID.randomUUID();

        UserDTO userDTO = new UserDTO(
                userId,
                "jan",
                "Jan",
                "Kowalski",
                "jan@test.pl",
                "Opis",
                null,
                4.8,
                10L,
                List.of()
        );

        List<UserDTO> users = List.of(userDTO);

        when(userService.getAllUsersWithAvgRatingAndFeatured("ARTIST"))
                .thenReturn(users);

        mockMvc.perform(
                get("/api/users")
                        .param("role", "ARTIST")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname")
                        .value("jan"));

        verify(userService)
                .getAllUsersWithAvgRatingAndFeatured("ARTIST");
    }
}
