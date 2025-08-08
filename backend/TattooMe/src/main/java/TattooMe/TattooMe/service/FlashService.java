package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.FlashDTO;
import TattooMe.TattooMe.entity.Flash;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.FlashRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class FlashService {
    @Autowired
    private final FlashRepository flashRepository;
    @Autowired
    private final UserRepository userRepository;
    public FlashService(FlashRepository flashRepository, UserRepository userRepository) {
        this.flashRepository = flashRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public void save(UUID userId, MultipartFile file, FlashDTO flashDTO) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        Flash flash = new Flash();
        flash.setUser(user);
        flash.setPicture(file.getBytes());
        flash.setSizeMin(flashDTO.sizeMin);
        flash.setSizeMax(flashDTO.sizeMax);
        flash.setPriceMin(flashDTO.priceMin);
        flash.setPriceMax(flashDTO.priceMax);
        flash.setReccomendedPlace(flashDTO.reccomendedPlace);
        flash.setDescription(flashDTO.description);
        flashRepository.save(flash);
    }

    public List<Flash> getUserFlashes(UUID userId) {
        return flashRepository.findAllByUser_Id(userId);
    }
}