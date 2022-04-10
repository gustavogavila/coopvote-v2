package com.gustavoavila.coopvote.domain.repository;

import com.gustavoavila.coopvote.domain.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
}
