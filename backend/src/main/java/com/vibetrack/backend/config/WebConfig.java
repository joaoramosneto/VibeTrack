package com.vibetrack.backend.config; // Use o seu nome de pacote

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // A URL que o front-end usará (ex: http://localhost:8080/fotos-perfil/minha-foto.jpg)
        String resourcePath = "/fotos-perfil/**";

        // O caminho físico no servidor onde as imagens estão salvas
        // "file:" é importante para indicar que é um caminho no sistema de arquivos
        String resourceLocation = "file:uploads/fotos-perfil/";

        registry.addResourceHandler(resourcePath)
                .addResourceLocations(resourceLocation);
    }
}