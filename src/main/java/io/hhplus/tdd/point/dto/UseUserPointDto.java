package io.hhplus.tdd.point.dto;

import lombok.Builder;

public class UseUserPointDto {

    @Builder
    public static record Request (
            long amount
    ) {}


    @Builder
    public static record Response (
            long id,
            long point,
            long updateMillis
    ) {}
}
