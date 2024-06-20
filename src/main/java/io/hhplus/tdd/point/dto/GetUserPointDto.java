package io.hhplus.tdd.point.dto;


import jdk.jshell.Snippet;
import lombok.Builder;

@Builder
public class GetUserPointDto {


    @Builder
    public static record Response (
            long id,
            long point,
            long updateMillis
    ) {
    }
}
