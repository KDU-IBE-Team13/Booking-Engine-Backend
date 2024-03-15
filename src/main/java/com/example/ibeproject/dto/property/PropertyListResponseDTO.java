package com.example.ibeproject.dto.property;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyListResponseDTO {
    private List<PropertyDTO> properties;
}
