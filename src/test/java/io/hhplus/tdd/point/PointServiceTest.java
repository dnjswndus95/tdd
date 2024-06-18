package io.hhplus.tdd.point;


import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointHistoriesDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.repository.UserPointRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private PointHistoryTable pointHistoryTable;

    @Autowired
    private UserPointTable userPointTable;


    @BeforeEach
    public void TableInit() {
        // 초기 데이터 삽입
        userPointTable.insertOrUpdate(1L, 10);
        userPointTable.insertOrUpdate(4L, 180);
        userPointTable.insertOrUpdate(5L, 50);
        pointHistoryTable.insert(2L, 50, TransactionType.CHARGE, System.currentTimeMillis());
        pointHistoryTable.insert(2L, 30, TransactionType.USE, System.currentTimeMillis());
    }

    @Test
    public void 유저포인트조회() throws Exception {
        // given
        // 서비스 함수의 파라미터로 넘어오는 id값
        long id = 1L;

        // when
        GetUserPointDto.Response res = this.pointService.getUserPoint(id);

        // then
        Assertions.assertThat(res.id()).isEqualTo(1L);
        Assertions.assertThat(res.point()).isEqualTo(10);
    }

    @Test
    public void 유저포인트_히스토리조회() {
        // given
        long id = 2L;

        // when
        GetUserPointHistoriesDto.Response res = this.pointService.getUserPointHistories(id);

        // then
        Assertions.assertThat(res.userPointHistories().get(0)).isEqualTo(new PointHistory(1L, 2L, 50, TransactionType.CHARGE, res.userPointHistories().get(0).updateMillis()));
        Assertions.assertThat(res.userPointHistories().get(1)).isEqualTo(new PointHistory(2L, 2L, 30L, TransactionType.USE, res.userPointHistories().get(1).updateMillis()));
    }

    @Test
    public void 유저포인트_충전_성공() throws Exception {
        // given
        long id = 3L;
        ChargeUserPointDto.Request request
                = ChargeUserPointDto.Request.builder()
                .amount(100L)
                .build();

        // when
        ChargeUserPointDto.Response res = this.pointService.chargeUserPoint(id, request);

        // then
        // 유저 포인트 충전 제대로 됐는지 검증
        Assertions.assertThat(res.id()).isEqualTo(3L);
        Assertions.assertThat(res.point()).isEqualTo(100L);

        // 포인트 히스토리 제대로 쌓였는지 검증
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(3L);
        PointHistory pointHistory = pointHistories.get(0);

        Assertions.assertThat(pointHistory).isEqualTo(new PointHistory(3L, 3L, 100L, TransactionType.CHARGE, pointHistory.updateMillis()));
    }

    @Test
    public void 유저포인트_충전_실패() throws Exception {
        // given
        // 실패 케이스 : amount 음수
        long id = 3L;
        ChargeUserPointDto.Request request
                = ChargeUserPointDto.Request.builder()
                .amount(-10L)
                .build();

        // when
        ChargeUserPointDto.Response res = this.pointService.chargeUserPoint(id, request);

        // then
        // Service 함수의 실패케이스를 작성하는데 밑의 검증 단계가 필요한지 질문 필요
        // 유저 포인트 충전 제대로 됐는지 검증
        Assertions.assertThat(res.id()).isEqualTo(3L);
        Assertions.assertThat(res.point()).isEqualTo(100L);

        // 포인트 히스토리 제대로 쌓였는지 검증
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(3L);
        PointHistory pointHistory = pointHistories.get(0);

        Assertions.assertThat(pointHistory.userId()).isEqualTo(3L);
        Assertions.assertThat(pointHistory.amount()).isEqualTo(-10L);
        Assertions.assertThat(pointHistory.type()).isEqualTo(TransactionType.CHARGE);
    }

    @Test
    public void 유저포인트_사용_성공() throws Exception {
        // given
        // 100포인트를 사용하는 것으로 가정
        long id = 4L;
        UseUserPointDto.Request request
                = UseUserPointDto.Request.builder()
                .amount(100L)
                .build();

        // when
        UseUserPointDto.Response res = this.pointService.useUserPoint(4L, request);

        // then
        // 유저 포인트 충전 제대로 됐는지 검증
        Assertions.assertThat(res.id()).isEqualTo(4L);
        Assertions.assertThat(res.point()).isEqualTo(80L);

        // 포인트 히스토리 제대로 쌓였는지 검증
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(4L);
        PointHistory pointHistory = pointHistories.get(0);

        Assertions.assertThat(pointHistory).isEqualTo(new PointHistory(pointHistory.id(), 4L, 100L, TransactionType.USE, pointHistory.updateMillis()));

    }
    @Test
    public void 유저포인트_사용_실패_많은포인트() throws Exception {
        // given
        // 50포인트 가지고 있는 사람이 100포인트를 사용하는 것으로 가정
        long id = 4L;
        UseUserPointDto.Request request
                = UseUserPointDto.Request.builder()
                .amount(100L)
                .build();

        // when
        UseUserPointDto.Response res = this.pointService.useUserPoint(5L, request);

        // then
        // Service 함수의 실패케이스를 작성하는데 밑의 검증 단계가 필요한지 질문 필요
        // 유저 포인트 충전 제대로 됐는지 검증
        Assertions.assertThat(res.id()).isEqualTo(5L);
        Assertions.assertThat(res.point()).isEqualTo(80L);

        // 포인트 히스토리 제대로 쌓였는지 검증
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(4L);
        PointHistory pointHistory = pointHistories.get(0);

        Assertions.assertThat(pointHistory).isEqualTo(new PointHistory(pointHistory.id(), 5L, 80L, TransactionType.USE, pointHistory.updateMillis()));
    }

    @Test
    public void 유저포인트_사용_실패_음수포인트() throws Exception {
        // given
        // -20포인트를 사용하는 것으로 가정
        long id = 4L;
        UseUserPointDto.Request request
                = UseUserPointDto.Request.builder()
                .amount(-20L)
                .build();

        // when
        UseUserPointDto.Response res = this.pointService.useUserPoint(5L, request);

        // then
        // Service 함수의 실패케이스를 작성하는데 밑의 검증 단계가 필요한지 질문 필요
        // 유저 포인트 충전 제대로 됐는지 검증
        Assertions.assertThat(res.id()).isEqualTo(5L);
        Assertions.assertThat(res.point()).isEqualTo(200L);

        // 포인트 히스토리 제대로 쌓였는지 검증
        List<PointHistory> pointHistories = pointHistoryTable.selectAllByUserId(4L);
        PointHistory pointHistory = pointHistories.get(0);

        Assertions.assertThat(pointHistory).isEqualTo(new PointHistory(pointHistory.id(), 5L, -20L, TransactionType.USE, pointHistory.updateMillis()));
    }
}
