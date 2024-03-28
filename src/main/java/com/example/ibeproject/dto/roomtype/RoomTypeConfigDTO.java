package com.example.ibeproject.dto.roomtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoomTypeConfigDTO {
    @JsonProperty("73")
    private RoomAmenities grandDeluxeAmenities;

    @JsonProperty("74")
    private RoomAmenities superDeluxeAmenities;

    @JsonProperty("75")
    private RoomAmenities familyDeluxeAmenities;

    @JsonProperty("76")
    private RoomAmenities coupleSuiteAmenities;

    @JsonProperty("77")
    private RoomAmenities gardenSuiteAmenities;

    @JsonProperty("78")
    private RoomAmenities standardSuiteAmenities;

    @Data
    public static class RoomAmenities {
        private String name;
        @JsonProperty("carouselImages")
        private List<String> carouselImages;
        private String description;
        private List<AmenityDTO> amenities;
    }
}
