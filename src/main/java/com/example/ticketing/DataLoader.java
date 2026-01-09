package com.example.ticketing;

import com.example.ticketing.entity.ConcertInfo;
import com.example.ticketing.repository.ConcertInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ConcertInfoRepository concertInfoRepository;
    private final RedisTemplate<String, String> redisTemplate; // Redis 도구 추가

    @Override
    public void run(String... args) throws Exception {
        // 기존 데이터가 있다면 초기화 (테스트를 위해)
        if (concertInfoRepository.count() == 0) {
            ConcertInfo concert = new ConcertInfo("아이유 콘서트", 100);
            ConcertInfo savedConcert = concertInfoRepository.save(concert);

            // ⭐ [핵심] Redis에 재고 저장!
            // Key: "concert_stock:1" / Value: "100"
            String key = "concert_stock:" + savedConcert.getId();
            redisTemplate.opsForValue().set(key, String.valueOf(100));

            System.out.println("🎁 초기 데이터 세팅 완료: DB & Redis (100석)");
        }
    }
}