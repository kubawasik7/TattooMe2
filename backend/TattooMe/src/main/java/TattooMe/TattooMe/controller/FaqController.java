package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.dto.faq.FaqDTO;
import TattooMe.TattooMe.service.FaqService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/studios/{studioId}/faq")
public class FaqController {
    @Autowired
    private FaqService faqService;

    @GetMapping
    public List<FaqDTO> getFaqs(@PathVariable UUID studioId) {
        return faqService.getFaqsByStudio(studioId);
    }

    @PostMapping
    public FaqDTO addFaq(@PathVariable UUID studioId, @RequestBody @Valid FaqDTO faqDTO) {
        return faqService.addFaq(studioId, faqDTO);
    }

    @DeleteMapping("/{faqId}")
    public void deleteFaq(@PathVariable UUID faqId) {
        faqService.deleteFaq(faqId);
    }
}
