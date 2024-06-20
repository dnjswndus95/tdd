package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserPointHistoryRepository {

    List<PointHistory> getUserPointHistories(long id);

    PointHistory insertPointHistory(long userId, long amount, TransactionType type, long updateMillis);
}
