package io.hhplus.tdd.repository;

import io.hhplus.tdd.point.UserPoint;

public class UserPointRepositoryStub implements UserPointRepository {

    @Override
    public UserPoint findByUserId(long id) {
        return new UserPoint(id, 100L, System.currentTimeMillis());
    }

    @Override
    public UserPoint chargeUserPoint(long id, long amount) {
        return new UserPoint(id, amount, System.currentTimeMillis());
    }

    @Override
    public UserPoint useUserPoint(long id, long amount) {
        return new UserPoint(id, amount, System.currentTimeMillis());
    }
}
