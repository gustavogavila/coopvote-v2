package com.gustavoavila.coopvote.domain.repository;

import com.gustavoavila.coopvote.domain.model.Agenda;
import com.gustavoavila.coopvote.domain.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByAssociateCPFAndAgenda(String associateCPF, Agenda agenda);
}
