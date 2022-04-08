package com.gustavoavila.coopvote.domain.model;

public class VotingResult {

    private String agendaDescription;
    private Long totalVotesYes;
    private Long totalVotesNo;
    private FinalResult finalResult;

    public VotingResult(String agendaDescription, Long totalVotesYes, Long totalVotesNo, FinalResult finalResult) {
        this.agendaDescription = agendaDescription;
        this.totalVotesYes = totalVotesYes;
        this.totalVotesNo = totalVotesNo;
        this.finalResult = finalResult;
    }

    public String getAgendaDescription() {
        return agendaDescription;
    }

    public Long getTotalVotesYes() {
        return totalVotesYes;
    }

    public Long getTotalVotesNo() {
        return totalVotesNo;
    }

    public FinalResult getFinalResult() {
        return finalResult;
    }
}
