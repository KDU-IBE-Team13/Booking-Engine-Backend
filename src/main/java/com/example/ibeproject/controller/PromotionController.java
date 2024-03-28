package com.example.ibeproject.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.ibeproject.dto.promotions.PromotionDTO;
import com.example.ibeproject.dto.promotions.PromotionResponseDTO;
import com.example.ibeproject.service.PromotionService;
@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {
  private final PromotionService promotionService;
   @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        List<PromotionDTO> promotionDetails = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotionDetails);
    }

    @GetMapping(value="applicable-promos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromotionResponseDTO> getApplicablePromotions(
        @RequestParam int tenantId,
        @RequestParam int propertyId,
        @RequestParam String checkInDate,
        @RequestParam String checkOutDate,
        @RequestParam(defaultValue = "false") Boolean isSeniorCitizen,
        @RequestParam(defaultValue = "false") Boolean isMilitaryPersonnel
    ) {
        PromotionResponseDTO promotionDetails = promotionService.getApplicablePromotions(tenantId, propertyId, checkInDate, checkOutDate, isSeniorCitizen, isMilitaryPersonnel);
        return ResponseEntity.ok(promotionDetails);
    }
}