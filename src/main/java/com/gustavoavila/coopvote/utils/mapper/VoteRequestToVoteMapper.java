package com.gustavoavila.coopvote.utils.mapper;

import com.gustavoavila.coopvote.domain.model.Vote;
import com.gustavoavila.coopvote.domain.model.VoteRequest;
import org.springframework.stereotype.Component;

@Component
public class VoteRequestToVoteMapper implements CoopVoteMapper<VoteRequest, Vote> {
    @Override
    public Vote transform(VoteRequest source) {
        return new Vote(source.getAssociateCPF(), source.getVoteValue(), source.getAgenda());
    }
}
