package com.gustavoavila.coopvote.domain.repository;

import com.gustavoavila.coopvote.domain.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query(value = "select\n" +
            "v.*\n" +
            "from vote v\n" +
            "inner join voting_session vs on v.voting_session_id = vs.id\n" +
            "inner join agenda a on vs.id = a.voting_session_id\n" +
            "where a.id = :agendaId\n" +
            "and v.associatecpf = :associateCPF", nativeQuery = true)
    Optional<List<Vote>> findByAssociateCPFAndAgenda(@Param("associateCPF") String associateCPF,
                                                     @Param("agendaId") Long agendaId);
}
