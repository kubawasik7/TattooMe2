package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.portfolio.PortfolioDTO;
import TattooMe.TattooMe.entity.Featured;
import TattooMe.TattooMe.entity.Portfolio;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.PortfolioMapper;
import TattooMe.TattooMe.repository.FeaturedRepository;
import TattooMe.TattooMe.repository.PortfolioRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortfolioMapper portfolioMapper;
    @Autowired
    private FeaturedRepository featuredRepository;

    public List<PortfolioDTO> getUserPortfolio(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        List<Portfolio> portfolioList = portfolioRepository.findAllByUser_Id(userId);

        List<Featured> featuredList = featuredRepository.findAllByArtistId(userId);

        Set<UUID> featuredIds = featuredList.stream()
                .map(f -> f.getPortfolio().getId())
                .collect(Collectors.toSet());

        return portfolioList.stream()
                .map(p -> {
                    PortfolioDTO dto = portfolioMapper.toDTO(p);
                    dto.setFeatured(featuredIds.contains(p.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
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

    @Transactional
    public void setFeatured(UUID userId, UUID itemId, boolean featured) {
        Portfolio item = portfolioRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zdjecia portfolio"));

        if (!item.getUser().getId().equals(userId)) {
            throw new RuntimeException("Nie jestes uprawniony");
        }

        if (featured) {
            long count = featuredRepository.countByArtistId(userId);
            if (count >= 5) {
                throw new RuntimeException("Maximum 5 featured items allowed");
            }

            Featured f = new Featured();
            f.setArtist(item.getUser());
            f.setPortfolio(item);
            featuredRepository.save(f);
        } else {
            featuredRepository.deleteByPortfolio(item);
        }
    }
}
