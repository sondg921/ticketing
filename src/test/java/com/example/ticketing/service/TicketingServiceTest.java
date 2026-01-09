package com.example.ticketing.service;

import com.example.ticketing.entity.ConcertInfo;
import com.example.ticketing.repository.ConcertInfoRepository;
import com.example.ticketing.repository.PaymentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TicketingServiceTest {
    @Autowired
    private TicketingService ticketingService;

    @Autowired
    private ConcertInfoRepository concertInfoRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("동시에 100명이 예매를 시도하면 재고가 정확히 0이 되어야 한다")
    void concurrencyTest() throws InterruptedException {
        // 1. 초기 데이터 설정 (100장 세팅)
        ConcertInfo concert = new ConcertInfo("아이유 콘서트", 100);
        ConcertInfo savedConcert = concertInfoRepository.save(concert);
        Long concertId = savedConcert.getId();

        //  테스트에서 만든 공연의 재고를 Redis에도 등록해야 함!
        redisTemplate.opsForValue().set("concert_stock:" + concertId, "100");

        // 2. 100명의 유저(스레드) 준비
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32); // 32개 스레드 풀
        CountDownLatch latch = new CountDownLatch(threadCount); // 100명이 끝날 때까지 기다리는 문지기

        // 3. 100명 동시 돌격
        for (int i = 0; i < threadCount; i++) {
            long userId = i + 1;
            executorService.submit(() -> {
                try{
                    ticketingService.issueTicket(userId, concertId);
                } finally{
                    latch.countDown(); // 한 명 끝날 때마다 카운트 감소
                }
            });
        }

        latch.await(); // 100명이 다 끝날 때까지 대기

        // 4. 결과 확인 (수정됨!)
        // DB 재고(ConcertInfo) 대신, 결제 내역(Payment) 개수를 셉니다.
        long totalPayments = paymentRepository.count();

        System.out.println("========================================");
        System.out.println("기대하는 결제 성공 수: 100");
        System.out.println("실제 결제 성공 수: " + totalPayments);
        System.out.println("========================================");

        assertEquals(100, totalPayments);
    }
}
