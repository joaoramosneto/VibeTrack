package com.vibetrack.backend.users.Repository;


import com.vibetrack.backend.users.Entity.DadoBiometrico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadoBiometricoRepository extends JpaRepository<DadoBiometrico, Long> {
}