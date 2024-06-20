package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPointRepository {

    UserPoint findByUserId(long id);

    UserPoint chargeUserPoint(long id, long amount);

    UserPoint useUserPoint(long id, long amount);

}
