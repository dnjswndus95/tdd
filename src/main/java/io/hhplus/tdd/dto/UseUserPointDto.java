package io.hhplus.tdd.dto;

public class UseUserPointDto {

    public record Request (
            long amount
    ) {}

    public record Response () {}
}
