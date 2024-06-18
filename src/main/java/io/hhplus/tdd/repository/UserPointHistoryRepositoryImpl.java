package io.hhplus.tdd.repository;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPointHistoryRepositoryImpl implements UserPointHistoryRepository {

    private final PointHistoryTable pointHistoryTable;

    @Override
    public List<PointHistory> getUserPointHistories(long id) {
        return this.pointHistoryTable.selectAllByUserId(id);
    }

    @Override
    public PointHistory insertPointHistory(long userId, long amount, TransactionType type, long updateMillis) {
        return this.pointHistoryTable.insert(userId, amount, type, updateMillis);
    }


}
