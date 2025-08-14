// PesquisadorRepository.java
package com.vibetrack.backend.users.Repository;

import com.vibetrack.backend.users.Entity.Pesquisador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesquisadorRepository extends JpaRepository<Pesquisador, Long> {
}