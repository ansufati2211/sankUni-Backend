package com.snkuni.sankuni.services;

import org.springframework.core.io.Resource;

public record ArchivoDescarga(Resource recurso, String nombreBase, String rutaAlmacenada) {
    public String nombreDescarga() {
        int puntoIdx = rutaAlmacenada.lastIndexOf('.');
        String extension = puntoIdx >= 0 ? rutaAlmacenada.substring(puntoIdx) : "";
        return nombreBase + extension;
    }
}
