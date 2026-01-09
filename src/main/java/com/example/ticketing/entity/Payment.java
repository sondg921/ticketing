package com.example.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_info_id")
    private ConcertInfo concertInfo;

    public Payment(Long userId, ConcertInfo concertInfo) {
        this.userId = userId;
        this.concertInfo = concertInfo;
    }
}
