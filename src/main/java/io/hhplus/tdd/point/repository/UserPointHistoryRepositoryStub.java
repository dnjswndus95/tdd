package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class UserPointHistoryRepositoryStub implements UserPointHistoryRepository {

    private Long cursor = 1L;

    @Override
    public List<PointHistory> getUserPointHistories(long id) {
        List<PointHistory> pointHistories = new ArrayList<>();
        pointHistories.add(new PointHistory(1L, 1L, 20L, TransactionType.CHARGE, System.currentTimeMillis()));
        pointHistories.add(new PointHistory(2L, 1L, 10L, TransactionType.USE, System.currentTimeMillis()));

        return pointHistories;
    }

    @Override
    public PointHistory insertPointHistory(long userId, long amount, TransactionType type, long updateMillis) {
        return new PointHistory(cursor++, userId, amount, type, updateMillis);
    }
}
