package io.hhplus.tdd.point.feature;

import io.hhplus.tdd.point.TransactionType;

public interface PointFeature {

    public long calculatePoint(long amount, long point, TransactionType type) throws Exception;
}
