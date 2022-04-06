package com.gustavoavila.coopvote.utils.mapper;

public interface CoopVoteMapper<T, U> {
    public U transform(T source);
}
