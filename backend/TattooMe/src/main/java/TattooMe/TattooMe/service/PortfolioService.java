package TattooMe.TattooMe.service;

import TattooMe.TattooMe.entity.Portfolio;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.PortfolioRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class PortfolioService {
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;
    public List<Portfolio> getUserPortfolio(UUID userId) {
        return portfolioRepository.findAllByUser_Id(userId);
    }
    public void uploadPortfolioImage(UUID userId, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony: " + userId));

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
        portfolioRepository.save(portfolio);
    }
    public void deletePortfolioImage(UUID id) {
        portfolioRepository.deleteById(id);
    }
}
