package com.vibetrack.backend.users.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    // Define o diretório onde as fotos serão salvas.
    // É uma boa prática colocar isso em 'application.properties'.
    private final Path rootLocation = Paths.get("uploads/fotos-perfil");

    public FileStorageService() {
        try {
            // Cria o diretório se ele não existir
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Falha ao armazenar arquivo vazio.");
        }

        try {
            // Pega o nome do arquivo
            String filename = file.getOriginalFilename();
            Path destinationFile = this.rootLocation.resolve(Paths.get(filename))
                    .normalize().toAbsolutePath();

            // Copia o conteúdo do arquivo para o destino
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Retorna o nome do arquivo para ser salvo no banco de dados
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar o arquivo.", e);
        }
    }
}