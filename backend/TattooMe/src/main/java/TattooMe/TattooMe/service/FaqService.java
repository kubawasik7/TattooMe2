package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.faq.FaqDTO;
import TattooMe.TattooMe.entity.Faq;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.mapper.FaqMapper;
import TattooMe.TattooMe.repository.FaqRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqService {
    @Autowired
    private FaqRepository faqRepository;
    @Autowired
    private TattooStudioRepository studioRepository;
    @Autowired
    private FaqMapper faqMapper;

    public List<FaqDTO> getFaqsByStudio(UUID studioId) {
        return faqRepository.findAllByTattooStudioId(studioId).stream()
                .map(faqMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FaqDTO addFaq(UUID studioId, FaqDTO faqDTO) {
        TattooStudio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));

        Faq faq = new Faq();
        faq.setQuestion(faqDTO.getQuestion());
        faq.setAnswer(faqDTO.getAnswer());
        faq.setTattooStudio(studio);

        return faqMapper.toDTO(faqRepository.save(faq));
    }

    public void deleteFaq(UUID faqId) {
        faqRepository.deleteById(faqId);
    }

}

