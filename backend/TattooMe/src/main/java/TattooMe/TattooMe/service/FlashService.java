package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.flash.CreateFlashDTO;
import TattooMe.TattooMe.dto.flash.FlashDTO;
import TattooMe.TattooMe.entity.Flash;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.FlashMapper;
import TattooMe.TattooMe.repository.FlashRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
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
public class FlashService {
    @Autowired
    private FlashRepository flashRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FlashMapper flashMapper;

    public List<FlashDTO> getUserFlashes(UUID userId) {
        return flashMapper.toDTOList(flashRepository.findAllByUser_Id(userId));
    }

    public List<FlashDTO> getFlashesForStudio(UUID studioId) {
        return flashRepository.findAllFlashesByStudioId(studioId)
                .stream()
                .map(flashMapper::toDTO)
                .toList();
    }

    @Transactional
    public FlashDTO addFlash(UUID userId, MultipartFile file, CreateFlashDTO dto) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika"));

        Flash flash = new Flash();
        flash.setUser(user);
        flash.setPicture(file.getBytes());
        flash.setSizeMin(dto.getSizeMin());
        flash.setSizeMax(dto.getSizeMax());
        flash.setPriceMin(dto.getPriceMin());
        flash.setPriceMax(dto.getPriceMax());
        flash.setReccomendedPlace(dto.getReccomendedPlace());
        flash.setDescription(dto.getDescription());

        Flash saved = flashRepository.save(flash);
        return flashMapper.toDTO(saved);
    }

    @Transactional
    public FlashDTO updateFlash(UUID id, CreateFlashDTO dto) {
        Flash flash = flashRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono wzoru"));

        flash.setSizeMin(dto.getSizeMin());
        flash.setSizeMax(dto.getSizeMax());
        flash.setPriceMin(dto.getPriceMin());
        flash.setPriceMax(dto.getPriceMax());
        flash.setReccomendedPlace(dto.getReccomendedPlace());
        flash.setDescription(dto.getDescription());

        Flash updated = flashRepository.save(flash);
        return flashMapper.toDTO(updated);
    }

    @Transactional
    public void deleteFlash(UUID id) {
        if (!flashRepository.existsById(id)) {
            throw new EntityNotFoundException("Nie znaleziono wzoru");
        }
        flashRepository.deleteById(id);
    }
}