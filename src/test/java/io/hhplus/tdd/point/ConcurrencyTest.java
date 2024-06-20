package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import io.hhplus.tdd.point.service.PointService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class ConcurrencyTest {

    @Autowired
    private PointService pointService;

    @Test
    void 입금_출금_동시성_테스트() throws Exception {
        // given
        Long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 0L, System.currentTimeMillis());

        // 테스트를 위한 사용자 ID 가정
        long id = 1L;
        // 충전과 사용을 각각 수행할 작업 수
        int numOfTasks = 5;

        // 초기 포인트 량
        long initPointAmount = 1000L;
        // 충전량
        long chargePointAmount = 100L;
        // 사용량
        long usePointAmount = 50L;

        // 초기 포인트 충전
        pointService.chargeUserPoint(userId,
                ChargeUserPointDto.Request.builder()
                .amount(initPointAmount)
                .build());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // when
        // 포인트 충전 작업
        for (int i = 0; i < numOfTasks; i++) {
            CompletableFuture<Void> chargeRequest = CompletableFuture.runAsync(() -> {
                try {
                    pointService.chargeUserPoint(userId,
                            ChargeUserPointDto.Request.builder()
                                    .amount(chargePointAmount)
                                    .build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            futures.add(chargeRequest);
        }

        // 포인트 사용 작업
        for (int i = 0; i < numOfTasks; i++) {
            CompletableFuture<Void> useRequest = CompletableFuture.runAsync(() -> {
                try {
                    pointService.useUserPoint(userId,
                            UseUserPointDto.Request.builder()
                                    .amount(usePointAmount)
                                    .build());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            futures.add(useRequest);
        }

        // 모든 작업이 완료될 때까지 기다림
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get();

        // 최종 포인트 잔액 검증
        GetUserPointDto.Response findUserPoint = pointService.getUserPoint(userId);
        Long expectedFinalAmount = initPointAmount + (chargePointAmount * numOfTasks) - (usePointAmount * numOfTasks);
        Assertions.assertThat(findUserPoint.point()).isEqualTo(expectedFinalAmount);
    }
}


