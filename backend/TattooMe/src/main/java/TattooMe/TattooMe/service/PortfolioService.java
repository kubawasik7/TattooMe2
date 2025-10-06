package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.PortfolioDTO;
import TattooMe.TattooMe.entity.Portfolio;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.PortfolioRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    public List<PortfolioDTO> getUserPortfolio(UUID userId) {
        return portfolioRepository.findAllByUser_Id(userId)
                .stream()
                .map(p -> new PortfolioDTO(
                        p.getId(),
                        Base64.getEncoder().encodeToString(p.getPicture())
                ))
                .toList();
    }

    public PortfolioDTO uploadPortfolioImage(UUID userId, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Plik nie może być pusty");
        }

        String contentType = multipartFile.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
            throw new IllegalArgumentException("Niedozwolony typ pliku: " + contentType);
        }

        Portfolio portfolio = new Portfolio();
        portfolio.setPicture(multipartFile.getBytes());
        portfolio.setUser(user);

        Portfolio saved = portfolioRepository.save(portfolio);

        return new PortfolioDTO(
                saved.getId(),
                Base64.getEncoder().encodeToString(saved.getPicture())
        );
    }

    public void deletePortfolioImage(UUID id) {
        portfolioRepository.deleteById(id);
    }
}
