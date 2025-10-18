package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.portfolio.PortfolioDTO;
import TattooMe.TattooMe.entity.Portfolio;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.PortfolioMapper;
import TattooMe.TattooMe.repository.PortfolioRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortfolioMapper portfolioMapper;

    public List<PortfolioDTO> getUserPortfolio(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        return portfolioMapper.toDTOList(portfolioRepository.findAllByUser_Id(userId));
    }

    @Transactional
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
        portfolio.setUser(user);
        portfolio.setPicture(multipartFile.getBytes());

        Portfolio saved = portfolioRepository.save(portfolio);
        return portfolioMapper.toDTO(saved);
    }

    @Transactional
    public void deletePortfolioImage(UUID id) {
        if (!portfolioRepository.existsById(id)) {
            throw new EntityNotFoundException("Nie znaleziono zdjęcia portfolio");
        }
        portfolioRepository.deleteById(id);
    }
}
