package com.vibetrack.backend.users.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Pega o email remetente do application.properties para evitar repetição
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async // Executa este método em uma thread separada
    public void enviarEmailDeBoasVindas(String paraEmail, String nomeUsuario) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(fromEmail);
            mensagem.setTo(paraEmail);
            mensagem.setSubject("Bem-vindo(a) ao VibeTrack!");

            String texto = String.format(
                    "Olá, %s!\n\nSeu cadastro na plataforma VibeTrack foi realizado com sucesso.\n\n" +
                            "Você já pode fazer login e começar a criar seus experimentos.\n\n" +
                            "Atenciosamente,\nA Equipe VibeTrack",
                    nomeUsuario
            );

            mensagem.setText(texto);
            mailSender.send(mensagem);

            System.out.println("Email de boas-vindas enviado para: " + paraEmail);

        } catch (Exception e) {
            // Em uma aplicação real, teríamos um log mais robusto aqui
            System.err.println("Erro ao enviar email de boas-vindas: " + e.getMessage());
        }
    }
}
