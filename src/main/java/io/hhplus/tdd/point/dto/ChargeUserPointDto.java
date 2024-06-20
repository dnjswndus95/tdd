package io.hhplus.tdd.point.dto;

import lombok.*;

@Getter
public class ChargeUserPointDto {

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
