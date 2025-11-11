package com.vibetrack.backend.users.Service;

import org.springframework.stereotype.Service;

@Service
public class AnalysisService {

    /**
     * Analisa o BPM e retorna uma emoção (lógica falsa).
     * Esta é a mesma lógica que faríamos em Python, mas em Java.
     *
     * @param bpm A média de batimentos por minuto.
     * @return Uma string descrevendo a emoção provável.
     */
    public String analisarEmocaoPorBpm(double bpm) {
        if (bpm < 60) {
            return "Relaxado Profundo / Sono";
        } else if (bpm >= 60 && bpm < 70) {
            return "Calmo / Relaxado";
        } else if (bpm >= 70 && bpm < 85) {
            return "Neutro / Focado";
        } else if (bpm >= 85 && bpm < 100) {
            return "Alerta / Possível Alegria";
        } else if (bpm >= 100 && bpm < 120) {
            return "Agitado / Estresse Leve";
        } else { // bpm >= 120
            return "Estresse Alto / Agitação / Raiva";
        }
    }
}