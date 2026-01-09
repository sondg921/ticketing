package com.example.ticketing.service;

import com.example.ticketing.entity.ConcertInfo;
import com.example.ticketing.entity.Payment;
import com.example.ticketing.repository.ConcertInfoRepository;
import com.example.ticketing.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketingService {
    private final ConcertInfoRepository concertInfoRepository;
    private final PaymentRepository paymentRepository;
    private final RedisTemplate<String, String> redisTemplate;

    //@Transactional  // 전체 트랜잭션을 걸면 Redis랑 섞여서 오히려 성능 저하될 수 있음. 저장할 때만 걸거나 범위를 조정.
    public Long issueTicket(Long userId, Long concertId) {
        String redisKey = "concert_stock:" + concertId;

        // 1. ⚡ Redis에서 재고 감소 (DECR 명령어)
        // decrement는 감소 후의 값을 리턴합니다. (100 -> 99 리턴)
        Long stock = redisTemplate.opsForValue().decrement(redisKey);

        // 2. 재고 검증
        if (stock < 0) {
            // 이미 매진된 상태
            // (선택사항) Redis 숫자가 음수로 계속 내려가는 게 싫다면 다시 increment 해줄 수도 있음
            throw new RuntimeException("매진되었습니다! (Redis)");
        }

        // 3. 🎫 성공한 사람만 DB에 결제 내역 저장
        // (여기서부터는 DB 접근이라 조금 느려도 됨, 이미 재고는 확보했으니까!)

        // 공연 정보는 단순히 참조용으로 조회 (Lock 필요 없음!)
        ConcertInfo concert = concertInfoRepository.findById(concertId)
                .orElseThrow(() -> new RuntimeException("공연 정보 없음"));

        Payment payment = new Payment(userId, concert);
        return paymentRepository.save(payment).getId();
    }
}
