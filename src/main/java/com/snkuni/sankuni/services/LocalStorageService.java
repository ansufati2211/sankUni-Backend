package com.snkuni.sankuni.services;

import com.snkuni.sankuni.exceptions.BusinessLogicException;
import com.snkuni.sankuni.exceptions.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private final Path raiz;

    public LocalStorageService(@Value("${sankuni.storage.location}") String location) {
        this.raiz = Paths.get(location).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(raiz.resolve("materiales"));
            Files.createDirectories(raiz.resolve("entregas"));
        } catch (IOException e) {
            throw new BusinessLogicException("No se pudo inicializar el almacenamiento de archivos.");
        }
    }

    @Override
    public String store(MultipartFile file, String subfolder) {
        if (file == null || file.isEmpty()) {
            throw new BusinessLogicException("El archivo enviado está vacío.");
        }
        String nombreOriginal = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
        String extension = "";
        int puntoIdx = nombreOriginal.lastIndexOf('.');
        if (puntoIdx >= 0) {
            extension = nombreOriginal.substring(puntoIdx);
        }
        String nombreArchivo = UUID.randomUUID() + extension;
        String rutaRelativa = subfolder + "/" + nombreArchivo;

        try {
            Path destino = raiz.resolve(rutaRelativa).normalize();
            file.transferTo(destino);
        } catch (IOException e) {
            throw new BusinessLogicException("No se pudo guardar el archivo enviado.");
        }
        return rutaRelativa;
    }

    @Override
    public Resource load(String rutaRelativa) {
        try {
            Path archivo = raiz.resolve(rutaRelativa).normalize();
            Resource resource = new UrlResource(archivo.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("El archivo solicitado no existe.");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("El archivo solicitado no existe.");
        }
    }

    @Override
    public void delete(String rutaRelativa) {
        try {
            Files.deleteIfExists(raiz.resolve(rutaRelativa).normalize());
        } catch (IOException e) {
            throw new BusinessLogicException("No se pudo eliminar el archivo.");
        }
    }
}
