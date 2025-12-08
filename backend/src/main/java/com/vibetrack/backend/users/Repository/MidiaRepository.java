package com.vibetrack.backend.users.Repository;

import com.vibetrack.backend.users.Entity.Midia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MidiaRepository extends JpaRepository<Midia, Long> {
    // O JpaRepository já nos dá métodos prontos como:
    // save(), findById(), delete(), etc.
}