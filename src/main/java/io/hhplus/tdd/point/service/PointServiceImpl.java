package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointHistoriesDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import io.hhplus.tdd.point.feature.PointFeature;
import io.hhplus.tdd.point.feature.PointFeatureImpl;
import io.hhplus.tdd.point.lock.LockManager;
import io.hhplus.tdd.point.repository.UserPointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final UserPointRepository userPointRepository;

    private final UserPointHistoryRepository userPointHistoryRepository;

    private final LockManager lockManager;

    private final PointFeature pointFeature = new PointFeatureImpl();

    @Override
    public GetUserPointDto.Response getUserPoint(long id) throws Exception {
        UserPoint findUserPoint = this.userPointRepository.findByUserId(id);

        // getOrDefault로 findUserPoint가 없는 경우는 없지만 예외처리 코드 작성
        if(null == findUserPoint)
            throw new Exception("UserPoint 조회 실패");

        return GetUserPointDto.Response
                .builder()
                .id(findUserPoint.id())
                .point(findUserPoint.point())
                .updateMillis(findUserPoint.updateMillis())
                .build();
    }

    @Override
    public GetUserPointHistoriesDto.Response getUserPointHistories(long id) {
        List<PointHistory> userPointHistories = this.userPointHistoryRepository.getUserPointHistories(id);

        return GetUserPointHistoriesDto.Response
                .builder()
                .userPointHistories(userPointHistories)
                .build();

    }

    @Override
    public ChargeUserPointDto.Response chargeUserPoint(long id, ChargeUserPointDto.Request request) throws Exception {
        return lockManager.executeOnLock(id, () -> {
            try {
                // 해당 id로 된 UserPoint 검색
                UserPoint findUserPoint = this.userPointRepository.findByUserId(id);

                // 포인트 충전량 예외 처리 및 포인트 충전
                long updatedUserPoint = this.pointFeature.calculatePoint(request.amount(), findUserPoint.point(), TransactionType.CHARGE);

                // 유저 포인트 충전
                // Map에 같은 key값으로 충전 완료된 포인트를 삽입
                UserPoint userPoint = this.userPointRepository.chargeUserPoint(id, updatedUserPoint);

                // 유저 포인트 충전한 히스토리 삽입
                PointHistory pointHistory = this.userPointHistoryRepository.insertPointHistory(id, request.amount(), TransactionType.CHARGE, System.currentTimeMillis());

                return ChargeUserPointDto.Response
                        .builder()
                        .id(userPoint.id())
                        .point(userPoint.point())
                        .updateMillis(0)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public UseUserPointDto.Response useUserPoint(long id, UseUserPointDto.Request request) throws Exception{
        return lockManager.executeOnLock(id, () -> {
            try {
                // 해당 id로 된 UserPoint 검색
                UserPoint findUserPoint = this.userPointRepository.findByUserId(id);

                // 포인트 사용 예외 처리 및 포인트 사용
                long updatedUserPoint = pointFeature.calculatePoint(request.amount(), findUserPoint.point(), TransactionType.USE);

                // 유저 포인트 사용
                // Map에 같은 key값으로 사용 완료된 포인트를 삽입
                UserPoint userPoint = this.userPointRepository.useUserPoint(id, updatedUserPoint);

                // 유저 포인트 사용한 히스토리 삽입
                PointHistory pointHistory = this.userPointHistoryRepository.insertPointHistory(id, request.amount(), TransactionType.USE, System.currentTimeMillis());


                return UseUserPointDto.Response
                        .builder()
                        .id(userPoint.id())
                        .point(userPoint.point())
                        .updateMillis(0)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
