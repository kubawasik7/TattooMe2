package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.portfolio.PortfolioDTO;
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

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PortfolioDTO>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(portfolioService.getUserPortfolio(userId));
    }

    @PostMapping("/upload")
    public ResponseEntity<PortfolioDTO> uploadPortfolioImage(
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails user) throws IOException {
        return ResponseEntity.ok(portfolioService.uploadPortfolioImage(user.getId(), file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        portfolioService.deletePortfolioImage(id);
        return ResponseEntity.noContent().build();
    }
}
