package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.ChatDTO;
import TattooMe.TattooMe.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {
    private final ChatService chatService;
    @GetMapping
    public List<ChatDTO> getUserChats(@AuthenticationPrincipal CustomUserDetails user) {
        return chatService.getUserChats(user.getId());
    }
    @PostMapping("/start")
    public ChatDTO startChat(@AuthenticationPrincipal CustomUserDetails user,
                             @RequestParam UUID receiverId) {
        return chatService.startChat(user.getId(), receiverId);
    }
}
