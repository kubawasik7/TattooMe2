package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.message.MessageDTO;
import TattooMe.TattooMe.dto.message.NewMessageDTO;
import TattooMe.TattooMe.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable UUID chatId,
                                        @AuthenticationPrincipal CustomUserDetails user) throws AccessDeniedException {
        return ResponseEntity.ok(messageService.getMessages(chatId, user.getId()));
    }

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody NewMessageDTO newMessageDTO,
                                                  @AuthenticationPrincipal CustomUserDetails user) throws AccessDeniedException {
        MessageDTO message = messageService.sendMessage(newMessageDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
