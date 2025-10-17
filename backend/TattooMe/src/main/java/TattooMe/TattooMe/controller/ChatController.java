package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.chat.ChatDTO;
import TattooMe.TattooMe.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatDTO>> getUserChats(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(chatService.getUserChats(user.getId()));
    }

    @PostMapping("/start")
    public ResponseEntity<ChatDTO> startChat(@AuthenticationPrincipal CustomUserDetails user,
                                             @RequestParam UUID receiverId) {
        ChatDTO chat = chatService.startChat(user.getId(), receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chat);
    }
}
