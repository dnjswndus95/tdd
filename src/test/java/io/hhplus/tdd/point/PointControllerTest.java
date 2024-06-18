package io.hhplus.tdd.point;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.tdd.point.controller.PointController;
import io.hhplus.tdd.point.dto.ChargeUserPointDto;
import io.hhplus.tdd.point.dto.UseUserPointDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(PointController.class)
public class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void 유저의_포인트_조회() throws Exception{
        long id = 1L;

        // 컨트롤러에 설정되어 있는 목데이터가 제대로 반환되는지 검증.
        this.mockMvc.perform(MockMvcRequestBuilders.get("/point/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(0)) // 반환되는 JSON의 필드 값 확인
                .andExpect(MockMvcResultMatchers.jsonPath("$.point").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updateMillis").value(0));

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
