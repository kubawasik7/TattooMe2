package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.entity.TattooStyle;
import TattooMe.TattooMe.service.WorkStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/styles")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkStyleController {

    @Autowired
    private WorkStyleService service;
    @GetMapping("/all")
    public List<TattooStyle> getAll() {
        List<TattooStyle> styles = service.getAllStyles();

        if (styles.isEmpty()) { // do usuniecia
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brak dostępnych stylów.");
        }
        return service.getAllStyles();
    }
    @GetMapping("/user/{id}")
    public List<TattooStyle> getForUser(@PathVariable UUID id) {
        return service.getUserStyles(id);
    }
    @PostMapping("/user/{id}")
    public void saveForUser(@PathVariable UUID id, @RequestBody List<UUID> styleIds) {
        service.saveUserStyles(id, styleIds);
    }
}