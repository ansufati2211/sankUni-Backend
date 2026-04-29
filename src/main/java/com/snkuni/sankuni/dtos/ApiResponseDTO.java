package com.snkuni.sankuni.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO {
    private boolean exito;
    private String mensaje;
    private Object data;
}