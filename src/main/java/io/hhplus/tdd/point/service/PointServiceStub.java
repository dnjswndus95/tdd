package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointHistoriesDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import io.hhplus.tdd.point.feature.PointFeature;
import io.hhplus.tdd.point.feature.PointFeatureImpl;
import io.hhplus.tdd.point.repository.UserPointRepositoryStub;

public class PointServiceStub implements PointService {

    private UserPointRepositoryStub userPointRepositoryStub = new UserPointRepositoryStub();
    private PointFeature pointFeature = new PointFeatureImpl();

    @Override
    public GetUserPointDto.Response getUserPoint(long id) throws Exception {
        UserPoint findUserPoint = userPointRepositoryStub.findByUserId(id);

        return GetUserPointDto.Response.builder()
                .id(findUserPoint.id())
                .point(findUserPoint.point())
                .updateMillis(findUserPoint.updateMillis())
                .build();
    }

    @Override
    public GetUserPointHistoriesDto.Response getUserPointHistories(long id) {
        return null;
    }

    @Override
    public ChargeUserPointDto.Response chargeUserPoint(long id, ChargeUserPointDto.Request request) throws Exception {
        UserPoint userPoint = new UserPoint(3L, 100L, System.currentTimeMillis());

        long updatedPoint = pointFeature.calculatePoint(request.amount(), userPoint.point(), TransactionType.CHARGE);

        return ChargeUserPointDto.Response.builder()
                .id(userPoint.id())
                .point(updatedPoint)
                .updateMillis(System.currentTimeMillis())
                .build();
    }

    @Override
    public UseUserPointDto.Response useUserPoint(long id, UseUserPointDto.Request request) throws Exception {
        UserPoint userPoint = new UserPoint(4L, 60L, System.currentTimeMillis());

        long updatedPoint = pointFeature.calculatePoint(request.amount(), userPoint.point(), TransactionType.USE);

        return UseUserPointDto.Response.builder()
                .id(4L)
                .point(updatedPoint)
                .updateMillis(userPoint.updateMillis())
                .build();
    }
}
