package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.MessageDTO;
import TattooMe.TattooMe.dto.NewMessageDTO;
import TattooMe.TattooMe.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    @GetMapping("/{chatId}")
    public List<MessageDTO> getMessages(@PathVariable UUID chatId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return messageService.getMessages(chatId, userDetails.getId());
    }
    @PostMapping
    public ResponseEntity<Void> sendMessage(@RequestBody NewMessageDTO newMessageDTO,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        messageService.sendMessage(newMessageDTO, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}
