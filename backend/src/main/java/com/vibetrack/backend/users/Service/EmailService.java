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

    @Async
    public void enviarEmailConfirmacaoExperimento(String paraEmail, String nomePesquisador, String nomeExperimento) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(fromEmail);
            mensagem.setTo(paraEmail);
            mensagem.setSubject("VibeTrack: Experimento Cadastrado com Sucesso!");

            String texto = String.format(
                    "Olá, %s!\n\nSeu experimento \"%s\" foi cadastrado com sucesso na plataforma VibeTrack.\n\n" +
                            "Você já pode visualizar os detalhes e adicionar participantes.\n\n" +
                            "Atenciosamente,\nA Equipe VibeTrack",
                    nomePesquisador,
                    nomeExperimento
            );

            mensagem.setText(texto);
            mailSender.send(mensagem);

            System.out.println("Email de confirmação de experimento enviado para: " + paraEmail);

        } catch (Exception e) {
            System.err.println("Erro ao enviar email de confirmação de experimento: " + e.getMessage());
        }
    }

    @Async
    public void enviarEmailDeVerificacao(String paraEmail, String nomeUsuario, String codigo) {
        try {
            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(fromEmail);
            mensagem.setTo(paraEmail);
            mensagem.setSubject("VibeTrack - Seu Código de Verificação");

            String texto = String.format(
                    "Olá, %s!\n\nSeu cadastro na plataforma VibeTrack está quase completo.\n\n" +
                            "Use o código abaixo para ativar sua conta. Este código é válido por 15 minutos.\n\n" +
                            "Seu código de verificação é: %s\n\n" +
                            "Atenciosamente,\nA Equipe VibeTrack",
                    nomeUsuario,
                    codigo
            );

            mensagem.setText(texto);
            mailSender.send(mensagem);

            System.out.println("Email de verificação enviado para: " + paraEmail);

        } catch (Exception e) {
            System.err.println("Erro ao enviar email de verificação: " + e.getMessage());
        }
    }

    // vvvv ESTE É O NOVO MÉTODO QUE ADICIONAMOS vvvv
    @Async
    public void enviarEmailDeReset(String paraEmail, String nomeUsuario, String token) {
        try {
            // IMPORTANTE: Este link aponta para o seu FRONTEND.
            // Nós ainda vamos criar esta página no Angular.
            String urlDeReset = "http://localhost:4200/auth/reset-password?token=" + token;

            SimpleMailMessage mensagem = new SimpleMailMessage();
            mensagem.setFrom(fromEmail);
            mensagem.setTo(paraEmail);
            mensagem.setSubject("VibeTrack - Redefinição de Senha");

            String texto = String.format(
                    "Olá, %s!\n\n" +
                            "Recebemos uma solicitação para redefinir sua senha na plataforma VibeTrack.\n\n" +
                            "Clique no link abaixo (ou copie e cole no seu navegador) para criar uma nova senha. Este link é válido por 1 hora.\n\n" +
                            "%s\n\n" +
                            "Se você não solicitou esta redefinição, por favor, ignore este e-mail.\n\n" +
                            "Atenciosamente,\nA Equipe VibeTrack",
                    nomeUsuario,
                    urlDeReset
            );

            mensagem.setText(texto);
            mailSender.send(mensagem);

            System.out.println("Email de redefinição de senha enviado para: " + paraEmail);

        } catch (Exception e) {
            System.err.println("Erro ao enviar email de redefinição: " + e.getMessage());
        }
    }
    // ^^^^ FIM DO NOVO MÉTODO ^^^^
}