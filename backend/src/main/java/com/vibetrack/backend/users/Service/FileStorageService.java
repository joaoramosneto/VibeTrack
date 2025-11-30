package com.vibetrack.backend.users.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID; // Importação para gerar IDs únicos

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads/fotos-perfil");

    public FileStorageService() {
        try {
            // Cria o diretório se ele não existir
            Files.createDirectories(rootLocation);
            System.out.println(">>> FILE STORAGE: Diretório de upload criado em: " + rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Falha ao armazenar arquivo vazio.");
        }

        try {
            System.out.println(">>> FILE STORAGE: Recebido arquivo: " + file.getOriginalFilename() + ". Tamanho: " + file.getSize() + " bytes.");

            // 1. Pega a extensão do arquivo (ex: .jpg)
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 2. Cria um nome de arquivo ÚNICO usando UUID
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFilename))
                    .normalize().toAbsolutePath();

            // 3. Copia o conteúdo do arquivo para o destino (usando o nome único)
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // 4. Log de Sucesso
            System.out.println(">>> FILE STORAGE: SUCESSO! Arquivo salvo como: " + uniqueFilename);

            // 5. Retorna o nome ÚNICO do arquivo
            return uniqueFilename;
        } catch (IOException e) {
            System.err.println(">>> FILE STORAGE: FALHA DE I/O: " + e.getMessage());
            throw new RuntimeException("Falha ao armazenar o arquivo.", e);
        }
    }
}