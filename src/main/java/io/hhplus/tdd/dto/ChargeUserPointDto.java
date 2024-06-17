package io.hhplus.tdd.dto;

public class ChargeUserPointDto {

    public record Request (
            long amount
    ) {}

    public record Response () {}
}
