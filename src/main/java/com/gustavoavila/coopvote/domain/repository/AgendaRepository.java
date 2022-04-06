package com.gustavoavila.coopvote.domain.repository;

import com.gustavoavila.coopvote.domain.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
