package com.snkuni.sankuni.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsistenciaDTO {
    private Long alumnoId;
    private Boolean presente;
}