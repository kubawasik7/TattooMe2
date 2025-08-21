package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.PersonInfoDTO;
import TattooMe.TattooMe.entity.PersonInfo;
import TattooMe.TattooMe.service.PersonInfoService;
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
    public ResponseEntity<PersonInfo> getInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return personInfoService.getUserInfo(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }

    @PutMapping
    public ResponseEntity<PersonInfo> updateInfo(@AuthenticationPrincipal CustomUserDetails user,
                                                 @RequestBody PersonInfoDTO dto) {
        return ResponseEntity.ok(personInfoService.updateUserInfo(user.getId(), dto));
    }

}
