package io.hhplus.tdd.point;

import io.hhplus.tdd.point.feature.PointFeature;
import io.hhplus.tdd.point.feature.PointFeatureImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PointFeatureTest {

    private final PointFeature pointFeature = new PointFeatureImpl();

    @Test
    public void 포인트계산_사용_성공() throws Exception {
        // given
        long point = 100;
        long amount = 60;

        // when
        // 포인트 충전
        long updatedPoint = pointFeature.calculatePoint(amount, point, TransactionType.USE);

        // then
        // 검증
        Assertions.assertThat(updatedPoint).isEqualTo(40);
    }

    @Test
    public void 포인트계산_충전_성공() throws Exception {
        // given
        long point = 100;
        long amount = 60;

        // when
        // 포인트 사용
        long updatedPoint = pointFeature.calculatePoint(amount, point, TransactionType.CHARGE);

        // then
        // 검증
        Assertions.assertThat(updatedPoint).isEqualTo(160);
    }

    @Test
    public void 포인트계산_충전_실패() throws Exception {
        // given
        // 충전량 음수 실패 case
        long point = 100;
        long amount = -10;

        // when
        // 포인트 사용
        long updatedPoint = pointFeature.calculatePoint(amount, point, TransactionType.CHARGE);

        // then
        // 검증
        Assertions.assertThat(updatedPoint).isEqualTo(90);
    }

    @Test
    public void 포인트계산_사용_실패_가진포인트보다_많은포인트_사용() throws Exception {
        // given
        // 가지고 있는 포인트보다 많은 포인트 사용 case
        long point = 40;
        long amount = 60;

        // when
        // 포인트 사용
        long updatedPoint = pointFeature.calculatePoint(amount, point, TransactionType.USE);

        // then
        // 검증
        Assertions.assertThat(updatedPoint).isEqualTo(-20);
    }

    @Test
    public void 포인트계산_사용_실패_음수포인트_사용() throws Exception {
        // given
        // 가지고 있는 포인트보다 많은 포인트 사용 case
        long point = 40;
        long amount = -10;

        // when
        // 포인트 사용
        long updatedPoint = pointFeature.calculatePoint(amount, point, TransactionType.USE);

        // then
        // 검증
        Assertions.assertThat(updatedPoint).isEqualTo(-20);
    }
}
