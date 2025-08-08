package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.entity.Portfolio;
import TattooMe.TattooMe.repository.PortfolioRepository;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioController {
    private PortfolioRepository portfolioRepository;
    private UserRepository userRepository;

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{userId}")
    public List<Portfolio> getByUser(@PathVariable UUID userId) {
        return portfolioService.getUserPortfolio(userId);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPortfolioImage(
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails principal) throws IOException {
        portfolioService.uploadPortfolioImage(principal.getId(), file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        portfolioService.deletePortfolioImage(id);
    }
}
