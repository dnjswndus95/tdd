package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointHistoriesDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import org.springframework.stereotype.Service;

@Service
public interface PointService {

    public GetUserPointDto.Response getUserPoint(long id) throws Exception;
    public GetUserPointHistoriesDto.Response getUserPointHistories(long id);
    public ChargeUserPointDto.Response chargeUserPoint(long id, ChargeUserPointDto.Request request) throws Exception;
    public UseUserPointDto.Response useUserPoint(long id, UseUserPointDto.Request request) throws Exception;
}
