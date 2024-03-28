package com.example.ibeproject.service;

import com.example.ibeproject.dto.promocode.PromoCodeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromoCodeService {

    private List<PromoCodeDTO> promoCodes;

    public PromoCodeService() {
        try {
            promoCodes = loadPromoCodesFromFile();
        } catch (IOException e) {
            e.printStackTrace();
           
        }
    }

    private List<PromoCodeDTO> loadPromoCodesFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource("/promocodeData.json");
        InputStream inputStream = resource.getInputStream();
        List<PromoCodeDTO> promoCodes = objectMapper.readValue(inputStream, new TypeReference<List<PromoCodeDTO>>() {});
        return promoCodes;
    }

    public List<PromoCodeDTO> getApplicablePromoCodes(int tenantId, int propertyId, String checkInDate, String checkOutDate, Boolean isDisabled) {
        

        String checkInDateSubstr = checkInDate.substring(0, 10);
        String checkOutDateSubstr = checkOutDate.substring(0, 10);

        LocalDate checkIn = LocalDate.parse(checkInDateSubstr);
        LocalDate checkOut = LocalDate.parse(checkOutDateSubstr);

        List<PromoCodeDTO> applicablePromoCodes = new ArrayList<>();

        for (PromoCodeDTO promoCode : promoCodes) {
            System.out.println(promoCode);
            if (isPromoCodeApplicable(promoCode, checkIn, checkOut)) {
                applicablePromoCodes.add(promoCode);
            }
        }

        if(!isDisabled)
        {
            applicablePromoCodes.removeIf(applicablePromoCode -> applicablePromoCode.getPromoCodeId() == 1);
        }

        System.out.println(applicablePromoCodes);
        return applicablePromoCodes;
    }

    private boolean isPromoCodeApplicable(PromoCodeDTO promoCode, LocalDate checkIn, LocalDate checkOut) {
        LocalDate expirationDate = LocalDate.parse(promoCode.getExpirationDate());

        return promoCode.isActive() &&
                LocalDate.now().isBefore(expirationDate) && 
                checkIn.compareTo(checkOut) < 0;
    }
}
