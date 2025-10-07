package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.FlashDTO;
import TattooMe.TattooMe.dto.OfferDTO;
import TattooMe.TattooMe.entity.Flash;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.FlashRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class FlashService {
    @Autowired
    private FlashRepository flashRepository;
    @Autowired
    private UserRepository userRepository;

    public List<FlashDTO> getUserFlashes(UUID userId) {
        return flashRepository.findAllByUser_Id(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public FlashDTO addFlash(UUID userId, MultipartFile file, FlashDTO flashDTO) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        Flash flash = new Flash();

        flash.setUser(user);
        flash.setPicture(file.getBytes());
        flash.setSizeMin(flashDTO.getSizeMin());
        flash.setSizeMax(flashDTO.getSizeMax());
        flash.setPriceMin(flashDTO.getPriceMin());
        flash.setPriceMax(flashDTO.getPriceMax());
        flash.setReccomendedPlace(flashDTO.getReccomendedPlace());
        flash.setDescription(flashDTO.getDescription());

        return toDto(flashRepository.save(flash));
    }

    public FlashDTO updateFlash(UUID id, FlashDTO flashDTO) {
        Flash flash = flashRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono wzoru"));

        flash.setSizeMin(flashDTO.getSizeMin());
        flash.setSizeMax(flashDTO.getSizeMax());
        flash.setPriceMin(flashDTO.getPriceMin());
        flash.setPriceMax(flashDTO.getPriceMax());
        flash.setReccomendedPlace(flashDTO.getReccomendedPlace());
        flash.setDescription(flashDTO.getDescription());

        Flash updated = flashRepository.save(flash);
        return toDto(updated);
    }

    public void deleteFlash(UUID id) {
        if (!flashRepository.existsById(id)) {
            throw new EntityNotFoundException("Nie znaleziono wzoru");
        }
        flashRepository.deleteById(id);
    }

    public FlashDTO toDto(Flash flash) {
        FlashDTO dto = new FlashDTO();
        dto.setSizeMin(flash.getSizeMin());
        dto.setSizeMax(flash.getSizeMax());
        dto.setPriceMin(flash.getPriceMin());
        dto.setPriceMax(flash.getPriceMax());
        dto.setReccomendedPlace(flash.getReccomendedPlace());
        dto.setDescription(flash.getDescription());
        dto.setPicture(Base64.getEncoder().encodeToString(flash.getPicture()));
        return dto;
    }

}