package io.hhplus.tdd.point;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.controller.PointController;
import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.GetUserPointDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;


@WebMvcTest(PointController.class)
public class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private PointService pointService;

    @Test
    public void 유저의_포인트_조회() throws Exception{
        // given
        long id = 1L;
        GetUserPointDto.Response res = GetUserPointDto.Response.builder()
                .id(1L)
                .point(100L)
                .updateMillis(0)
                .build();
        given(pointService.getUserPoint(id)).willReturn(res);

        // when
        // then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/point/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L)) // 반환되는 JSON의 필드 값 확인
                .andExpect(MockMvcResultMatchers.jsonPath("$.point").value(100L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 유저의_이용내역_조회() throws Exception {
        long id = 1L;

        this.mockMvc.perform(MockMvcRequestBuilders.get("/point/{id}/histories", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 유저의_포인트_충전() throws Exception {
        long id = 1L;

        // dto 생성 및 JSON으로 파싱
        ChargeUserPointDto.Request request = new ChargeUserPointDto.Request(100);
        String requestJson = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/point/{id}/charge", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 유저의_포인트_사용() throws Exception {
        long id = 1L;

        UseUserPointDto.Request request = new UseUserPointDto.Request(20);
        String requestJson = this.objectMapper.writeValueAsString(request);

        this.mockMvc.perform(MockMvcRequestBuilders.patch("/point/{id}/use", id)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0)) // 반환되는 JSON의 필드 값 확인
                .andExpect(MockMvcResultMatchers.jsonPath("$.point").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateMillis").value(0));
    }
}
