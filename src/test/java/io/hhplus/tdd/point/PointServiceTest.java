package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import io.hhplus.tdd.point.service.PointServiceStub;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PointServiceTest {

    PointServiceStub pointServiceStub;
    @BeforeEach
    void Init() {
        pointServiceStub = new PointServiceStub();
    }

    @Test
    public void 포인트조회() throws Exception {
        Long id = 2L;

        GetUserPointDto.Response findUserPoint = pointServiceStub.getUserPoint(id);

        Assertions.assertThat(findUserPoint.point()).isEqualTo(100L);
    }

    @Test
    public void 포인트충전_성공() throws Exception {
        Long id = 3L;
        ChargeUserPointDto.Request request
                = ChargeUserPointDto.Request.builder()
                .amount(100L)
                .build();

        ChargeUserPointDto.Response response = pointServiceStub.chargeUserPoint(id, request);

        Assertions.assertThat(response.point()).isEqualTo(200L);
    }

    @Test
    public void 포인트충전_실패() throws Exception {
        Long id = 3L;
        ChargeUserPointDto.Request request
                = ChargeUserPointDto.Request.builder()
                .amount(-20L)
                .build();

         Exception exception = assertThrows(Exception.class, () -> pointServiceStub.chargeUserPoint(id, request));

         Assertions.assertThat(exception.getMessage()).isEqualTo("포인트 충전은 양수만 가능합니다.");
    }

    @Test
    public void 포인트사용_성공() throws Exception {
        Long id = 4L;
        UseUserPointDto.Request request
                = UseUserPointDto.Request.builder()
                .amount(30)
                .build();

        UseUserPointDto.Response response = pointServiceStub.useUserPoint(id, request);

        Assertions.assertThat(response.point()).isEqualTo(30L);
    }

    @Test
    public void 포인트사용_실패_가진것보다_많은포인트사용() throws Exception {
        Long id = 4L;
        UseUserPointDto.Request request
                = UseUserPointDto.Request.builder()
                .amount(80)
                .build();

        Exception exception = assertThrows(Exception.class, () -> pointServiceStub.useUserPoint(id, request));

        Assertions.assertThat(exception.getMessage()).isEqualTo("가진 포인트보다 많이 사용할 수 없습니다.");
    }

    @Test
    public void 포인트사용_실패_음수포인트_사용() throws Exception {
        // given
        Long id = 4L;
        UseUserPointDto.Request request
                = UseUserPointDto.Request.builder()
                .amount(-30)
                .build();

        // when
        // 음수 포인트 사용으로 Exception 던짐.
        Exception exception = assertThrows(Exception.class, () -> pointServiceStub.useUserPoint(id, request));

        // then
        // 음수 포인트 사용에 맞는 Exception이 떨어졌는지 검증.
        Assertions.assertThat(exception.getMessage()).isEqualTo("사용 포인트는 양수여야 합니다.");
    }


}
