package io.hhplus.tdd.point.feature;

import io.hhplus.tdd.point.TransactionType;

public class PointFeatureImpl implements PointFeature {

    @Override
    public long calculatePoint(long amount, long point, TransactionType type) throws Exception {
        if(TransactionType.USE == type) {
            if(point < amount)
                throw new Exception("가진 포인트보다 많이 사용할 수 없습니다.");
            else if(0 > amount)
                throw new Exception("사용 포인트는 양수여야 합니다.");
            return point - amount;
        }
        else if(TransactionType.CHARGE == type) {
            if(0 > amount)
                throw new Exception("포인트 충전은 양수만 가능합니다.");
            return point + amount;
        }
        else
            throw new Exception("계산타입은 더하기, 빼기만 가능합니다.");
    }
}
