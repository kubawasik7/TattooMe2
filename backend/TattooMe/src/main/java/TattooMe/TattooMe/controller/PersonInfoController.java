package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.PersonInfoDTO;
import TattooMe.TattooMe.service.PersonInfoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personinfo")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonInfoController {
    private final PersonInfoService personInfoService;

    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @GetMapping("/me")
    public ResponseEntity<PersonInfoDTO> getInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return personInfoService.getUserInfo(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<PersonInfoDTO> updateInfo(@AuthenticationPrincipal CustomUserDetails user,
                                                    @RequestBody @Valid PersonInfoDTO dto) {
        return ResponseEntity.ok(personInfoService.updateUserInfo(user.getId(), dto));
    }
}
