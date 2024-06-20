package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.PointHistory;
import lombok.Builder;

import java.util.List;

public class GetUserPointHistoriesDto {

    @Builder
    public static record Response (
            List<PointHistory> userPointHistories
    ) {}
}
