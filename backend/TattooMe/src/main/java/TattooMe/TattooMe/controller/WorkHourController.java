package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.dto.workHour.WorkHourDTO;
import TattooMe.TattooMe.service.WorkHourService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/studios")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkHourController {
    @Autowired
    private WorkHourService workHourService;

    @GetMapping("/{studioId}/work-hours")
    public ResponseEntity<List<WorkHourDTO>> getWorkHours(@PathVariable UUID studioId) {
        return ResponseEntity.ok(workHourService.getWorkHoursForStudio(studioId));
    }

    @PostMapping("/{studioId}/work-hours")
    public ResponseEntity<WorkHourDTO> addWorkHour(@PathVariable UUID studioId, @RequestBody WorkHourDTO dto) {
        return ResponseEntity.ok(workHourService.addWorkHourToStudio(studioId, dto));
    }

    @PutMapping("/work-hours/{workHourId}")
    public ResponseEntity<WorkHourDTO> updateWorkHour(@PathVariable UUID workHourId, @RequestBody WorkHourDTO dto) {
        return ResponseEntity.ok(workHourService.updateWorkHour(workHourId, dto));
    }

    @DeleteMapping("/{studioId}/work-hours/{workHourId}")
    public ResponseEntity<Void> deleteWorkHour(@PathVariable UUID studioId, @PathVariable UUID workHourId) {
        workHourService.removeWorkHourFromStudio(studioId, workHourId);
        return ResponseEntity.noContent().build();
    }
}
