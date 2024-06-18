package io.hhplus.tdd.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserPointRepository {

    private final UserPointTable userPointTable;

    @Override
    public UserPoint findByUserId(long id) {
        return userPointTable.selectById(id);
    }

    @Override
    public UserPoint chargeUserPoint(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

    @Override
    public UserPoint useUserPoint(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }
}
