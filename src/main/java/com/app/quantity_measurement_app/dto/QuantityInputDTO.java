package com.app.quantity_measurement_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    @NotNull
    @Valid
    private QuantityDTO thisQuantityDTO;

    @NotNull
    @Valid
    private QuantityDTO thatQuantityDTO;
}
