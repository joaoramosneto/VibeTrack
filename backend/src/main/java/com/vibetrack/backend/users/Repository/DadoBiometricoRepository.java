package com.vibetrack.backend.users.Repository;


import com.vibetrack.backend.users.Entity.DadoBiometrico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DadoBiometricoRepository extends JpaRepository<DadoBiometrico, Long> {
    List<DadoBiometrico> findByExperimentoIdAndTipoDadoOrderByTimestampAsc(Long experimentoId, String tipoDado);
}