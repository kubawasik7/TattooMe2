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
        PersonInfo dto = personInfoService.getUserInfo(user.getId());
        System.out.println("Zwracam DTO: " + dto);
        System.out.println(dto.getAllergies());
        return ResponseEntity.ok(personInfoService.getUserInfo(user.getId()));
    }

    @PutMapping
    public ResponseEntity<PersonInfo> updateInfo(@AuthenticationPrincipal CustomUserDetails user,
                                                 @RequestBody PersonInfoDTO dto) {
        return ResponseEntity.ok(personInfoService.updateUserInfo(user.getId(), dto));
    }

}
